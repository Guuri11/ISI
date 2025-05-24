import { View } from "react-native";

import { Card, CardContent, CardHeader, CardTitle } from "~/presentation/components/ui/card";
import { Text } from "~/presentation/components/ui/text";
import { i18n } from "~/presentation/lib/locales/i18n";
import { useUserStore } from "~/presentation/lib/stores/user-store";

export default function Screen() {
    const { user } = useUserStore();

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
                        {i18n.t("hello", { name: user?.username })}
                    </CardTitle>
                </CardHeader>

                <CardContent>
                    <View style={{ padding: 12, borderRadius: 8, marginBottom: 16 }}>
                        <Text style={{ textAlign: "center" }}>
                            {i18n.t("this-is-your-profile")}
                        </Text>
                    </View>
                </CardContent>
            </Card>
        </View>
    );
}
