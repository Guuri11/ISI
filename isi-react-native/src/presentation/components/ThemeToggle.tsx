import { Pressable, View } from "react-native";

import { MoonStar } from "~/presentation/assets/icons/MoonStar";
import { Sun } from "~/presentation/assets/icons/Sun";
import { setAndroidNavigationBar } from "~/presentation/lib/android-navigation-bar";
import { useColorScheme } from "~/presentation/lib/hooks/useColorScheme";
import { cn } from "~/presentation/lib/utils";

export function ThemeToggle() {
    const { isDarkColorScheme, setColorScheme } = useColorScheme();

    function toggleColorScheme() {
        const newTheme = isDarkColorScheme ? "light" : "dark";
        setColorScheme(newTheme);
        setAndroidNavigationBar(newTheme);
    }

    return (
        <Pressable
            onPress={toggleColorScheme}
            className="web:ring-offset-background web:transition-colors web:focus-visible:outline-none web:focus-visible:ring-2 web:focus-visible:ring-ring web:focus-visible:ring-offset-2"
        >
            {({ pressed }) => (
                <View
                    className={cn(
                        "flex-1 aspect-square pt-0.5 justify-center items-start web:px-5",
                        pressed && "opacity-70",
                    )}
                >
                    {isDarkColorScheme ? (
                        <MoonStar className="text-foreground" size={23} strokeWidth={1.25} />
                    ) : (
                        <Sun className="text-foreground" size={24} strokeWidth={1.25} />
                    )}
                </View>
            )}
        </Pressable>
    );
}
