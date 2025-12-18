import React, { createContext, useContext, useState, useEffect } from "react";

export interface UserDTO {
  id: number;
  customerId: string;
  username: string;
  name: string;
  email: string;
  birthdate: string;
  roles: string[];
}

interface AuthContextType {
  user: UserDTO | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  getAuthHeader: () => string;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserDTO | null>(null);
  const [credentials, setCredentials] = useState<{
    username: string;
    password: string;
  } | null>(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    const savedCreds = localStorage.getItem("credentials");

    if (savedUser && savedCreds) {
      setUser(JSON.parse(savedUser));
      setCredentials(JSON.parse(savedCreds));
    }
  }, []);

  const login = async (email: string, password: string) => {
    const response = await fetch("http://localhost:8083/api/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      throw new Error("Invalid credentials");
    }

    const userData: UserDTO = await response.json();

    setUser(userData);
    setCredentials({ username: userData.username, password });

    localStorage.setItem("user", JSON.stringify(userData));
    localStorage.setItem(
      "credentials",
      JSON.stringify({ username: userData.username, password })
    );
  };

  const logout = () => {
    setUser(null);
    setCredentials(null);
    localStorage.removeItem("user");
    localStorage.removeItem("credentials");
  };

  const getAuthHeader = (): string => {
    if (!credentials) return "";
    const encoded = btoa(`${credentials.username}:${credentials.password}`);
    return `Basic ${encoded}`;
  };

  const isAdmin = user?.roles?.includes("ADMIN") ?? false;

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isAdmin,
        login,
        logout,
        getAuthHeader,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
