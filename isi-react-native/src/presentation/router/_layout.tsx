import "~/presentation/assets/global.css";

import {
    NunitoSans_300Light,
    NunitoSans_400Regular,
    NunitoSans_700Bold,
    useFonts,
} from "@expo-google-fonts/nunito-sans";
import { DarkTheme, DefaultTheme, Theme, ThemeProvider } from "@react-navigation/native";
import { PortalHost } from "@rn-primitives/portal";
import { Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import { useEffect, useLayoutEffect, useRef, useState } from "react";
import { Platform } from "react-native";

import { ThemeToggle } from "~/presentation/components/ThemeToggle";
import { setAndroidNavigationBar } from "~/presentation/lib/android-navigation-bar";
import { NAV_THEME } from "~/presentation/lib/constants";
import { useColorScheme } from "~/presentation/lib/hooks/useColorScheme";

const LIGHT_THEME: Theme = {
    ...DefaultTheme,
    colors: NAV_THEME.light,
};
const DARK_THEME: Theme = {
    ...DarkTheme,
    colors: NAV_THEME.dark,
};

export {
    // Catch any errors thrown by the Layout component.
    ErrorBoundary,
} from "expo-router";

export default function RootLayout() {
    const [fontsLoaded] = useFonts({
        NunitoSans_300Light,
        NunitoSans_400Regular,
        NunitoSans_700Bold,
    });
    const hasMounted = useRef(false);
    const { colorScheme, isDarkColorScheme } = useColorScheme();
    const [isColorSchemeLoaded, setIsColorSchemeLoaded] = useState(false);

    useIsomorphicLayoutEffect(() => {
        if (hasMounted.current) {
            return;
        }

        if (Platform.OS === "web") {
            // Adds the background color to the html element to prevent white background on overscroll.
            document.documentElement.classList.add("bg-background");
        }
        setAndroidNavigationBar(colorScheme);
        setIsColorSchemeLoaded(true);
        hasMounted.current = true;
    }, []);

    if (!isColorSchemeLoaded || !fontsLoaded) {
        return null;
    }

    return (
        <ThemeProvider value={isDarkColorScheme ? DARK_THEME : LIGHT_THEME}>
            <StatusBar style={isDarkColorScheme ? "light" : "dark"} />
            <Stack>
                <Stack.Screen
                    name="/index"
                    options={{
                        title: "Starter Base",
                        headerRight: () => <ThemeToggle />,
                    }}
                />
            </Stack>
            <PortalHost />
        </ThemeProvider>
    );
}

const useIsomorphicLayoutEffect =
    Platform.OS === "web" && typeof window === "undefined" ? useEffect : useLayoutEffect;
