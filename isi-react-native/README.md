# 📱 React Native Starter - Onboarding

This project is a skeleton for **React Native + Expo** designed to create robust, scalable, and modern applications. It implements:

- **Hexagonal Architecture & DDD**
- **Nativewind + Tailwind for reusable styles**
- **Zustand for Store management**
- **Sentry for monitoring**
- **I18n for internationalization**

## 🚀 Technologies included

- **[Expo SDK](https://docs.expo.dev/)**
- **[NativeWind](https://nativewindui.com/) + TailwindCSS**
- **[Zustand](https://github.com/pmndrs/zustand)**
- **TypeScript**

### Zustand

**Zustand** is a simple and fast state management library. It's used to manage the application's global state in a way that minimizes re-renders and simplifies the flow of data.

## 📂 Learn more about each folder

- [application](./src/application/README.md)
- [domain](./src/domain/README.md)
- [infrastructure](./src/infrastructure/README.md)
- [presentation](./src/presentation/README.md)

## 💻 Set up

- [Install NVM](https://github.com/nvm-sh/nvm)
- Recommended IDE: [VS Code](https://code.visualstudio.com/)
- [Install Expo Go app in your mobile device](https://expo.dev/go) to try locally
- Install docker (for lower RAM consume, I suggest install docker without docker desktop in your WSL & use docker vs code extensions instead)

## VS Code recommended extensions

- Docker
- Dev Containers
- ES7 + React/Redux/React-Native snippets
- Expo Tools
- Gitlens
- PostCSS Intellisense and Highlighting
- React Native Tools
- WSL
- Eslint
- Prettier ESLint
- Javascript and Typescript Nightly
- i18n-ally
- Color Highlight
- VScode Google Translate
- Pretty Typescript Errors
- Todo Tree

## 🔧 Quick Installation

```bash
git clone <repo-url>
cd <project>
npm install # or yarn
npm run dev # or npm run dev -- --tunnel if you're using WSL
```

You can access the app with Expo Go on your mobile or at http://localhost:8081/ from the web. Here's the use case you'll experience:

- A user wants to log in.
- They enter their username and password, fetched from (https://fakestoreapi.com/users/1).
- The state machine will control the flow and handle all possible scenarios.
- If everything is okay, the user will be redirected to another screen that simulates their profile. If there's an error, it will be shown on the screen.

If you want to extend or modify the use case, you can play around with the following files and folders:

- **domain**  
  You can modify the **business logic** and **domain models** here. Add new models, use cases, or customize error handling as needed. If the business flow requires a new model or validation logic, this is where you should work.

- **application**  
  Here, you can define **use cases** and manage the interaction between the domain and infrastructure. You can create or modify services that handle business logic.

- **infrastructure**  
  This folder contains the **concrete implementations** of the contracts defined in the domain. You can modify or add **adapters** and **repositories** to interact with external services like APIs, databases, or local storage. It should never contain business logic, only interactions with external services.

- **presentation**  
  In this folder, you can **create and modify UI components** (using the [React Native Reusables](https://rnr-docs.vercel.app) documentation), **add new routes with Expo Router**, and **manage state with Zustand**. If you need to handle complex UI flows or navigation, you can integrate **XState** to control the state and different scenarios (like user validation). Here, you can change how the user interacts with the interface and manage the flow between screens or states.

This is a starting point to customize the use case and build new features. Feel free to experiment and adjust the code as needed!

General project structure, main configurations:

- `app.json`, `package.json`, `tsconfig.json`: global configurations.
- `babel.config.js`, `metro.config.js`: setup for Expo and absolute paths.
- **Important**: Do not place any app logic or code here.

## 🔧 Best Practices

- Follow the **Hexagonal Architecture** by keeping business logic in the **domain** layer and interactions with external services in the **infrastructure** layer.
- Write small, focused components in the **presentation** layer.
- Keep your **Zustand** store simple and focused on global state.

### Notes:

There are ESLint rules based on the naming conventions and architecture. To check if the code complies with these rules, you can run the following command:

```
npx eslint .
```

To view the rules, go to [.eslintrc.js](.eslintrc.js), where you’ll see the rules to follow in order to comply with the hexagonal architecture.

You will find some code snippets to improve your Development Experience, checkout [snippets.json](.vscode/snippets.code-snippets)

### Some upgrades guidelines

- [Expo](https://docs.expo.dev/workflow/upgrading-expo-sdk-walkthrough/)
- [Nativewind](https://www.nativewind.dev/blog)
