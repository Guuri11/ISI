import { User } from "~/domain/models/user";

export interface UserRepositoryI {
    login: (username: string, password: string) => Promise<string | null>;
    getUserById: (id: number) => Promise<User | null>;
}
