import { User } from "~/domain/models/user";
import { UserRepositoryI } from "~/domain/repositories/user-repository";

import { FakeStoreApi } from "../api/fake-store-api";

export const UserRepository = (): UserRepositoryI => ({
    /**
     * @param {string} username
     * @param {string} password
     * @returns {Promise<string | null>} Resolves with the authentication token or null if login fails.
     */
    login: async (username: string, password: string): Promise<string | null> => {
        return FakeStoreApi.login(username, password);
    },

    /**
     * @param {number} id - The user's ID.
     * @returns {Promise<User | null>} Resolves with the user object or null if not found.
     */
    getUserById: async (id: number): Promise<User | null> => {
        return FakeStoreApi.getUserById(id);
    },
});
