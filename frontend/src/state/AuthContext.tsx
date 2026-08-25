import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import type { AuthResponse, UserResponse } from '../types';
import { login as loginApi, register as registerApi, fetchUserProfile } from '../api';

type AuthContextValue = {
  token: string | null;
  user: UserResponse | null;
  login: (payload: { email: string; password: string }) => Promise<void>;
  register: (payload: { name: string; email: string; password: string }) => Promise<void>;
  logout: () => void;
  loading: boolean;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('devflow_token'));
  const [user, setUser] = useState<UserResponse | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!token) {
      setUser(null);
      return;
    }

    const loadUser = async () => {
      try {
        const profile = await fetchUserProfile(token);
        setUser(profile);
      } catch {
        setToken(null);
        localStorage.removeItem('devflow_token');
      }
    };

    loadUser();
  }, [token]);

  const login = useCallback(async (payload: { email: string; password: string }) => {
    setLoading(true);
    try {
      const response = await loginApi(payload);
      localStorage.setItem('devflow_token', response.accessToken);
      if (response.refreshToken) {
        localStorage.setItem('devflow_refresh_token', response.refreshToken);
      }
      setToken(response.accessToken);
      const profile = await fetchUserProfile(response.accessToken);
      setUser(profile);
    } finally {
      setLoading(false);
    }
  }, []);

  const register = useCallback(async (payload: { name: string; email: string; password: string }) => {
    setLoading(true);
    try {
      const response = await registerApi(payload);
      localStorage.setItem('devflow_token', response.accessToken);
      if (response.refreshToken) {
        localStorage.setItem('devflow_refresh_token', response.refreshToken);
      }
      setToken(response.accessToken);
      const profile = await fetchUserProfile(response.accessToken);
      setUser(profile);
    } finally {
      setLoading(false);
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('devflow_token');
    localStorage.removeItem('devflow_refresh_token');
    setToken(null);
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({ token, user, login, register, logout, loading }),
    [token, user, login, register, logout, loading],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
}
