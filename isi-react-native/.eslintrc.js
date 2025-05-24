// https://docs.expo.dev/guides/using-eslint/
// TODO: Review plugins with package.json
module.exports = {
    extends: [
        "expo",
        "prettier",
        "eslint:recommended",
        "plugin:@typescript-eslint/recommended",
        "plugin:prettier/recommended",
    ],
    plugins: [
        "@typescript-eslint",
        "prettier",
        "import",
        "simple-import-sort",
        "filenames",
        "boundaries",
    ],
    settings: {
        react: {
            version: "detect",
        },
        "boundaries/elements": [
            { type: "domain", pattern: "src/domain/**" },
            { type: "application", pattern: "src/application/**" },
            { type: "infrastructure", pattern: "src/infrastructure/**" },
            { type: "presentation", pattern: "src/presentation/**" },
        ],
    },
    rules: {
        "prettier/prettier": "error",
        "simple-import-sort/imports": "error",
        "simple-import-sort/exports": "error",
        "@typescript-eslint/naming-convention": [
            "error",
            { selector: "default", format: ["camelCase"] },
            { selector: "variableLike", format: ["camelCase"] },
            { selector: "function", format: ["camelCase", "PascalCase"] },
            {
                selector: "variable",
                modifiers: ["const"],
                format: ["camelCase", "UPPER_CASE", "PascalCase"],
            },
            { selector: "typeLike", format: ["PascalCase"] },
            {
                selector: "interface",
                format: ["PascalCase"],
                custom: { regex: "^I[A-Z]", match: false },
            },
            { selector: "enumMember", format: ["UPPER_CASE"] },
            { selector: "import", format: null }, // https://github.com/typescript-eslint/typescript-eslint/issues/7838
            { selector: "objectLiteralProperty", format: null },
        ],
        "boundaries/element-types": [
            "error",
            {
                default: "disallow",
                rules: [
                    { from: "domain", allow: ["domain"] },
                    { from: "application", allow: ["domain", "application"] },
                    { from: "infrastructure", allow: ["domain", "application", "infrastructure"] },
                    {
                        from: "presentation",
                        allow: ["domain", "application", "infrastructure", "presentation"],
                    },
                ],
            },
        ],
    },
    overrides: [
        {
            files: ["*.ts"],
            rules: {
                "filenames/match-regex": [
                    "error",
                    "^[a-z0-9]+(-[a-z0-9]+)*$", // kebab-case
                    true,
                ],
            },
        },
        {
            files: ["*.tsx"],
            rules: {
                "filenames/match-regex": [
                    "error",
                    "^([A-Z][a-zA-Z0-9]*|[a-z][a-zA-Z0-9]*)$", // PascalCase o camelCase
                    true,
                ],
            },
        },
    ],
    ignorePatterns: ["/dist/*", "/*.config.js", "/.eslintrc.js", "/*.d.ts", "/index.js"],
};
