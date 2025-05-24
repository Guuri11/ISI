// Import 'clsx', a utility to conditionally join CSS class names into a single string.
// 'ClassValue' is a type exported by 'clsx' that represents any valid class value (string, object, array, etc.).
import { type ClassValue, clsx } from "clsx";
// Import 'twMerge', a function that intelligently merges Tailwind CSS classes,
// ensuring that conflicting classes are resolved properly (e.g., 'p-2 p-4' becomes 'p-4').
import { twMerge } from "tailwind-merge";

/**
 * Utility function 'cn' (short for 'class names') to combine CSS classes in a clean way.
 *
 * - Accepts multiple class names (strings, objects, arrays) thanks to 'ClassValue[]'.
 * - Uses 'clsx' to process and filter out falsy values like undefined or false.
 * - Passes the result to 'twMerge' to merge Tailwind classes and resolve conflicts.
 *
 * Example usage:
 * const buttonClass = cn('btn', condition && 'btn-primary', 'p-2', 'p-4');
 * Result: 'btn btn-primary p-4' (if condition is true)
 */
export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs));
}

/**
 * Checks if a given value is a valid value of an enum.
 *
 * @param value - The value to check.
 * @param enumObj - The enum to check the value against.
 * @returns `true` if the value is a valid value of the enum, `false` otherwise.
 */
export function isEnum(value: any, enumObj: object): boolean {
    return Object.values(enumObj).includes(value);
}
