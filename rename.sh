#!/usr/bin/env bash
#
# Creates a new project from the Keel template.
#
#   ./rename.sh <ProjectName> <root.package> <ios.bundle.id>
#   ./rename.sh MyApp com.example.myapp com.example.myapp
#
# - replaces the root package in the code, the Gradle namespaces and the Android applicationId
# - moves the Kotlin sources of every module / source set to match the new package
# - renames the project (settings.gradle.kts), the Android and iOS display names
# - sets the iOS bundle identifier (iosApp/Configuration/Config.xcconfig)
# - runs the Android and iOS verification builds
#
# Current values are read from the project itself, so the script can be run again later.
# Works with BSD sed (macOS) and GNU sed.

set -euo pipefail

usage() {
    echo "Usage: $0 <ProjectName> <root.package> <ios.bundle.id>" >&2
    echo "Example: $0 MyApp com.example.myapp com.example.myapp" >&2
    exit 1
}

fail() {
    echo "error: $*" >&2
    exit 1
}

[[ $# -eq 3 ]] || usage

NEW_NAME="$1"
NEW_PACKAGE="$2"
NEW_BUNDLE_ID="$3"

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

XCCONFIG="iosApp/Configuration/Config.xcconfig"
XCODE_PROJECT="iosApp/iosApp.xcodeproj"

# --- Validation -------------------------------------------------------------------------------

[[ -f settings.gradle.kts && -f gradle.properties && -f "$XCCONFIG" ]] ||
    fail "run this script from the root of a Keel project"

[[ "$NEW_NAME" =~ ^[A-Za-z][A-Za-z0-9]*$ ]] ||
    fail "invalid project name '$NEW_NAME' (letters and digits only, starting with a letter)"

[[ "$NEW_PACKAGE" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]] ||
    fail "invalid package '$NEW_PACKAGE' (lowercase segments separated by dots, e.g. com.example.app)"

KOTLIN_KEYWORDS=" as break class continue do else false for fun if in interface is null object package return super this throw true try typealias typeof val var when while "
IFS='.' read -r -a PACKAGE_SEGMENTS <<< "$NEW_PACKAGE"
for segment in "${PACKAGE_SEGMENTS[@]}"; do
    [[ "$KOTLIN_KEYWORDS" != *" $segment "* ]] || fail "package segment '$segment' is a Kotlin keyword"
done

[[ "$NEW_BUNDLE_ID" =~ ^[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)+$ ]] ||
    fail "invalid bundle identifier '$NEW_BUNDLE_ID' (e.g. com.example.app)"

OLD_PACKAGE="$(sed -n 's/^keel\.basePackage=//p' gradle.properties)"
OLD_NAME="$(sed -n 's/^rootProject\.name = "\(.*\)"$/\1/p' settings.gradle.kts)"
OLD_BUNDLE_ID="$(sed -n 's/^PRODUCT_BUNDLE_IDENTIFIER=//p' "$XCCONFIG")"

[[ -n "$OLD_PACKAGE" ]] || fail "keel.basePackage not found in gradle.properties"
[[ -n "$OLD_NAME" ]] || fail "rootProject.name not found in settings.gradle.kts"
[[ -n "$OLD_BUNDLE_ID" ]] || fail "PRODUCT_BUNDLE_IDENTIFIER not found in $XCCONFIG"

echo "Project name : $OLD_NAME -> $NEW_NAME"
echo "Package      : $OLD_PACKAGE -> $NEW_PACKAGE"
echo "iOS bundle id: $OLD_BUNDLE_ID -> $NEW_BUNDLE_ID"

# --- Helpers ----------------------------------------------------------------------------------

if sed --version >/dev/null 2>&1; then
    sed_inplace() { sed -i -E "$@"; }      # GNU sed
else
    sed_inplace() { sed -i '' -E "$@"; }   # BSD sed (macOS)
fi

escape_regex() { printf '%s' "$1" | sed 's/[][\.*^$/+?(){}|]/\\&/g'; }
escape_replacement() { printf '%s' "$1" | sed 's/[\/&]/\\&/g'; }

# Text files of the project, without build outputs, VCS and IDE data.
project_files() {
    find . \
        \( -name .git -o -name .gradle -o -name .kotlin -o -name .idea -o -name build -o -name DerivedData \
           -o -name xcuserdata \) -prune -o \
        -type f ! -name rename.sh -print |
        while IFS= read -r file; do
            grep -Iq . "$file" 2>/dev/null && printf '%s\n' "$file"
        done
}

# --- 1. Package -------------------------------------------------------------------------------

if [[ "$OLD_PACKAGE" != "$NEW_PACKAGE" ]]; then
    echo "Replacing package in sources and build files..."
    old_package_re="$(escape_regex "$OLD_PACKAGE")"
    new_package_rep="$(escape_replacement "$NEW_PACKAGE")"
    project_files | while IFS= read -r file; do
        if grep -q "$OLD_PACKAGE" "$file"; then
            sed_inplace "s/${old_package_re}([^A-Za-z0-9_]|\$)/${new_package_rep}\1/g" "$file"
        fi
    done

    echo "Moving source directories..."
    old_path="${OLD_PACKAGE//.//}"
    new_path="${NEW_PACKAGE//.//}"
    # Collect first: directories are moved while iterating.
    source_dirs=()
    while IFS= read -r dir; do source_dirs+=("$dir"); done < <(
        find . \( -name .git -o -name build -o -name .gradle -o -name .kotlin \) -prune -o \
            -type d -path "*/kotlin/$old_path" -print
    )
    for dir in ${source_dirs[@]+"${source_dirs[@]}"}; do
        source_root="${dir%/"$old_path"}"
        tmp_dir="$(mktemp -d "$source_root/.rename.XXXXXX")"
        find "$dir" -mindepth 1 -maxdepth 1 -exec mv {} "$tmp_dir"/ \;
        # Remove the now empty package directories (e.g. com/keel/app, com/keel, com).
        rmdir_path="$old_path"
        while [[ "$rmdir_path" != "." && -d "$source_root/$rmdir_path" ]] &&
            rmdir "$source_root/$rmdir_path" 2>/dev/null; do
            rmdir_path="$(dirname "$rmdir_path")"
        done
        mkdir -p "$source_root/$new_path"
        find "$tmp_dir" -mindepth 1 -maxdepth 1 -exec mv {} "$source_root/$new_path"/ \;
        rmdir "$tmp_dir"
    done

    # Room exported schemas are stored in folders named after the database class.
    schema_dirs=()
    while IFS= read -r dir; do schema_dirs+=("$dir"); done < <(
        find . \( -name .git -o -name build \) -prune -o -type d -path "*/schemas/$OLD_PACKAGE.*" -print
    )
    for dir in ${schema_dirs[@]+"${schema_dirs[@]}"}; do
        mv "$dir" "$(dirname "$dir")/$NEW_PACKAGE${dir##*/"$OLD_PACKAGE"}"
    done
fi

# --- 2. Project and display names ------------------------------------------------------------

echo "Renaming project..."
new_name_rep="$(escape_replacement "$NEW_NAME")"
old_name_re="$(escape_regex "$OLD_NAME")"

sed_inplace "s/^rootProject\.name = \".*\"\$/rootProject.name = \"${new_name_rep}\"/" settings.gradle.kts
sed_inplace "s/^PRODUCT_NAME=.*\$/PRODUCT_NAME=${new_name_rep}/" "$XCCONFIG"
find . \( -name .git -o -name build \) -prune -o -type f -name strings.xml -print |
    while IFS= read -r file; do
        sed_inplace "s|<string name=\"app_name\">[^<]*</string>|<string name=\"app_name\">${new_name_rep}</string>|" "$file"
    done
find "$XCODE_PROJECT" \( -name project.pbxproj -o -name '*.xcscheme' \) -not -path '*/xcuserdata/*' |
    while IFS= read -r file; do
        sed_inplace "s/(^|[^A-Za-z0-9_])${old_name_re}\.app/\1${new_name_rep}.app/g" "$file"
    done

# --- 3. iOS bundle identifier ----------------------------------------------------------------

echo "Setting iOS bundle identifier..."
sed_inplace "s/^PRODUCT_BUNDLE_IDENTIFIER=.*\$/PRODUCT_BUNDLE_IDENTIFIER=$(escape_replacement "$NEW_BUNDLE_ID")/" "$XCCONFIG"

# --- 4. Verification builds ------------------------------------------------------------------

echo "Removing stale build outputs..."
find . \( -name .git -o -name src \) -prune -o -type d -name build -print | while IFS= read -r dir; do
    [[ -f "$(dirname "$dir")/build.gradle.kts" ]] && rm -rf "$dir"
done
rm -rf .kotlin

echo "Building Android app..."
./gradlew :androidApp:assembleDebug

echo "Building iOS framework..."
./gradlew :shell:linkDebugFrameworkIosSimulatorArm64

if command -v xcodebuild >/dev/null 2>&1; then
    echo "Building iOS app..."
    xcodebuild -project "$XCODE_PROJECT" -scheme iosApp -sdk iphonesimulator \
        -destination 'generic/platform=iOS Simulator' build -quiet
else
    echo "xcodebuild not found: skipping the iOS app build."
fi

echo "Done: $NEW_NAME ($NEW_PACKAGE, iOS bundle id $NEW_BUNDLE_ID)."
