/// <reference types="vite/client" />

declare module '*.svg';
declare module '*.png';
declare module '*.jpg';
declare module '*.jpeg';
declare module '*.gif';

declare interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string;
}

declare interface ImportMeta {
  readonly env: ImportMetaEnv;
}
