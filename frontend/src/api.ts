import type { AnalyticsData, AuthResponse, LoginRequest, Notification, Project, Task, UserResponse } from './types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '';

function createHeaders(token?: string) {
  const headers = new Headers();
  headers.set('Accept', 'application/json');
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }
  return headers;
}

async function request<T>(path: string, options: RequestInit = {}, token?: string): Promise<T> {
  const url = API_BASE_URL ? `${API_BASE_URL}${path}` : path;
  const headers = createHeaders(token);

  if (options.body && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json');
  }

  const response = await fetch(url, {
    ...options,
    headers,
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    throw new Error(data?.message || `${response.status} ${response.statusText}`);
  }

  return data as T;
}

export function login(payload: LoginRequest): Promise<AuthResponse> {
  return request<AuthResponse>('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function register(payload: { name: string; email: string; password: string }): Promise<AuthResponse> {
  return request<AuthResponse>('/api/auth/register', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

export function fetchAnalytics(token?: string): Promise<AnalyticsData> {
  return request<AnalyticsData>('/api/analytics/team', undefined, token);
}

export function fetchProjects(token?: string): Promise<Project[]> {
  return request<Project[]>('/api/projects', undefined, token);
}

export function createProject(payload: { name: string; description?: string; status?: string; startDate?: string; endDate?: string }, token?: string): Promise<Project> {
  return request<Project>('/api/projects', {
    method: 'POST',
    body: JSON.stringify(payload),
  }, token);
}

export function fetchTasks(token?: string): Promise<Task[]> {
  return request<Task[]>('/api/tasks', undefined, token);
}

export function createTask(payload: { title: string; description?: string; priority?: string; status?: string; dueDate?: string; projectId?: number; assigneeId?: number; reporterId?: number }, token?: string): Promise<Task> {
  return request<Task>('/api/tasks', {
    method: 'POST',
    body: JSON.stringify(payload),
  }, token);
}

export function fetchUserProfile(token: string): Promise<UserResponse> {
  return request<UserResponse>('/api/profile', undefined, token);
}

export function fetchUsers(token?: string): Promise<UserResponse[]> {
  return request<UserResponse[]>('/api/users', undefined, token);
}

export function fetchNotifications(userId: number, token?: string): Promise<Notification[]> {
  return request<Notification[]>(`/api/notifications/user/${userId}`, undefined, token);
}
