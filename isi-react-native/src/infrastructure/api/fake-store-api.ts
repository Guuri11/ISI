import { User } from "~/domain/models/user";

type ApiResponse<T> = Promise<T | null>;

const host = process.env.EXPO_PUBLIC_API_URL;

/**
 * @param url
 * @param options Fetch options (optional).
 * @returns Parsed JSON response or null if an error occurs.
 */
const fetchWrapper = async <T>(url: string, options?: RequestInit): ApiResponse<T> => {
    try {
        const response = await fetch(url, options);

        if (!response.ok) {
            console.error(`API Error: ${response.status} ${response.statusText}`);
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error("Network error:", error);
        return null;
    }
};

export const FakeStoreApi = {
    /**
     * https://fakestoreapi.com/docs#tag/Auth
     * @param {string} username
     * @param {string} password
     * @returns {Promise<string | null>} Resolves with the authentication token or null if login fails.
     */
    login: async (username: string, password: string): ApiResponse<string> => {
        const data = await fetchWrapper<{ token?: string }>(`${host}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        return data?.token ?? null;
    },

    /**
     * https://fakestoreapi.com/docs#tag/Users/operation/getUserById
     * @param {number} id
     * @returns {Promise<User | null>} Resolves with the user object or null if not found.
     */
    getUserById: async (id: number): ApiResponse<User> => {
        return fetchWrapper<User>(`${host}/users/${id}`);
    },
};
