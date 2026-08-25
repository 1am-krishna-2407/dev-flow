import { BrowserRouter, Route, Routes } from 'react-router-dom';
import DashboardPage from './pages/DashboardPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProjectPage from './pages/ProjectPage';
import TaskPage from './pages/TaskPage';
import AnalyticsPage from './pages/AnalyticsPage';
import NotFoundPage from './pages/NotFoundPage';
import AppShell from './components/AppShell';
import RequireAuth from './components/RequireAuth';
import { AuthProvider } from './state/AuthContext';

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route
            element={
              <RequireAuth>
                <AppShell />
              </RequireAuth>
            }
          >
            <Route path="/" element={<DashboardPage />} />
            <Route path="/projects" element={<ProjectPage />} />
            <Route path="/tasks" element={<TaskPage />} />
            <Route path="/analytics" element={<AnalyticsPage />} />
          </Route>

          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
