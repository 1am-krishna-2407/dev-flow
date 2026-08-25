export type AuthResponse = {
  accessToken: string;
  refreshToken?: string;
  tokenType: string;
};

export type UserResponse = {
  id: number;
  name: string;
  email: string;
  role: string;
  department?: string;
  designation?: string;
  skills?: string;
  profilePictureUrl?: string;
  active: boolean;
  emailVerified: boolean;
};

export type ProjectStatus = 'PLANNING' | 'ACTIVE' | 'ON_HOLD' | 'COMPLETED';
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export type TaskStatus = 'BACKLOG' | 'TODO' | 'IN_PROGRESS' | 'REVIEW' | 'DONE';

export type LoginRequest = {
  email: string;
  password: string;
};

export type AnalyticsData = {
  projectCount: number;
  taskCount: number;
  userCount: number;
  completedTasks: number;
};

export type Project = {
  id: number;
  name: string;
  description?: string;
  status?: string;
  startDate?: string;
  endDate?: string;
};

export type Task = {
  id: number;
  title: string;
  description?: string;
  priority?: string;
  status?: string;
  dueDate?: string;
  assignee?: {
    id: number;
    name: string;
  };
  reporter?: {
    id: number;
    name: string;
  };
  project?: {
    id: number;
    name: string;
  };
};

export type Notification = {
  id: number;
  type?: string;
  message?: string;
  readFlag?: boolean;
  createdAt?: string;
  recipient?: {
    id: number;
    name: string;
  };
};
