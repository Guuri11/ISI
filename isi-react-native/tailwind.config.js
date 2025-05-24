const { hairlineWidth, platformSelect } = require("nativewind/theme");

/** @type {import('tailwindcss').Config} */
module.exports = {
    darkMode: "class",
    content: [
        "./src/presentation/router/**/*.{ts,tsx}",
        "./src/presentation/components/**/*.{ts,tsx}",
    ],
    presets: [require("nativewind/preset")],
    theme: {
        extend: {
            colors: {
                border: "hsl(var(--border))",
                input: "hsl(var(--input))",
                ring: "hsl(var(--ring))",
                background: "hsl(var(--background))",
                foreground: "hsl(var(--foreground))",
                primary: {
                    DEFAULT: "hsl(var(--primary))",
                    foreground: "hsl(var(--primary-foreground))",
                },
                secondary: {
                    DEFAULT: "hsl(var(--secondary))",
                    foreground: "hsl(var(--secondary-foreground))",
                },
                destructive: {
                    DEFAULT: "hsl(var(--destructive))",
                    foreground: "hsl(var(--destructive-foreground))",
                },
                muted: {
                    DEFAULT: "hsl(var(--muted))",
                    foreground: "hsl(var(--muted-foreground))",
                },
                accent: {
                    DEFAULT: "hsl(var(--accent))",
                    foreground: "hsl(var(--accent-foreground))",
                },
                popover: {
                    DEFAULT: "hsl(var(--popover))",
                    foreground: "hsl(var(--popover-foreground))",
                },
                card: {
                    DEFAULT: "hsl(var(--card))",
                    foreground: "hsl(var(--card-foreground))",
                },
            },
            fontFamily: {
                "nunito-thin": ["NunitoSans_200ExtraLight"],
                "nunito-light": ["NunitoSans_300Light"],
                nunito: ["NunitoSans_400Regular"],
                "nunito-medium": ["NunitoSans_500Medium"],
                "nunito-semibold": ["NunitoSans_600SemiBold"],
                "nunito-bold": ["NunitoSans_700Bold"],
                "nunito-extrabold": ["NunitoSans_800ExtraBold"],
                "nunito-black": ["NunitoSans_900Black"],
                "nunito-thin-italic": ["NunitoSans_200ExtraLight_Italic"],
                "nunito-light-italic": ["NunitoSans_300Light_Italic"],
                "nunito-italic": ["NunitoSans_400Regular_Italic"],
                "nunito-medium-italic": ["NunitoSans_500Medium_Italic"],
                "nunito-semibold-italic": ["NunitoSans_600SemiBold_Italic"],
                "nunito-bold-italic": ["NunitoSans_700Bold_Italic"],
                "nunito-extrabold-italic": ["NunitoSans_800ExtraBold_Italic"],
                "nunito-black-italic": ["NunitoSans_900Black_Italic"],
            },
            borderWidth: {
                hairline: hairlineWidth(),
            },
            keyframes: {
                "accordion-down": {
                    from: { height: "0" },
                    to: { height: "var(--radix-accordion-content-height)" },
                },
                "accordion-up": {
                    from: { height: "var(--radix-accordion-content-height)" },
                    to: { height: "0" },
                },
            },
            animation: {
                "accordion-down": "accordion-down 0.2s ease-out",
                "accordion-up": "accordion-up 0.2s ease-out",
            },
        },
    },
    plugins: [require("tailwindcss-animate")],
};
