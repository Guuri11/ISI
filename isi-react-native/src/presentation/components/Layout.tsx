import React, { PropsWithChildren } from "react";
import { SafeAreaView, StyleSheet, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const Layout = ({ children }: PropsWithChildren) => {
    const insets = useSafeAreaInsets();

    return (
        <SafeAreaView style={{ flex: 1, paddingTop: insets.top }}>
            <View style={styles.container}>{children}</View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    safeArea: {
        flex: 1,
    },
    container: {
        flex: 1,
        paddingHorizontal: 16,
        paddingTop: 8,
        paddingBottom: 8,
    },
});

export default Layout;
