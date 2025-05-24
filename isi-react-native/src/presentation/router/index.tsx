import { useRouter } from "expo-router";
import { useEffect, useState } from "react";
import { View } from "react-native";

import { Card, CardContent, CardHeader, CardTitle } from "~/presentation/components/ui/card";
import { Input } from "~/presentation/components/ui/input";
import { Text } from "~/presentation/components/ui/text";

import { Button } from "../components/ui/button";
import { i18n } from "../lib/locales/i18n";
import { useUserStore } from "../lib/stores/user-store";

export default function Screen() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const { user } = useUserStore();
    const router = useRouter();

    const handleSubmit = () => {};

    useEffect(() => {
        if (user?.id) {
            router.replace(`./users/${user?.id}`);
        }
    }, [router, user]);

    return (
        <View
            style={{
                flex: 1,
                justifyContent: "center",
                padding: 24,
            }}
        >
            <Card>
                <CardHeader>
                    <CardTitle style={{ textAlign: "center", fontSize: 24 }}>
                        {i18n.t("login.title")}
                    </CardTitle>
                    <Text style={{ textAlign: "center", marginTop: 4 }}>
                        {i18n.t("login.subtitle")}
                    </Text>
                </CardHeader>

                <CardContent>
                    <Input
                        placeholder={i18n.t("login.usernamePlaceholder")}
                        value={username}
                        onChangeText={setUsername}
                        autoCapitalize="none"
                        style={{
                            marginBottom: 16,
                            borderRadius: 8,
                            borderWidth: 1,
                            padding: 12,
                        }}
                    />
                    <Input
                        placeholder={i18n.t("login.passwordPlaceholder")}
                        value={password}
                        onChangeText={setPassword}
                        secureTextEntry
                        autoCapitalize="none"
                        style={{
                            marginBottom: 24,
                            borderRadius: 8,
                            borderWidth: 1,
                            padding: 12,
                        }}
                    />
                    <Button
                        onPress={handleSubmit}
                        style={{
                            paddingVertical: 14,
                            borderRadius: 8,
                        }}
                    >
                        <Text>Submit</Text>
                    </Button>
                </CardContent>
            </Card>
        </View>
    );
}
