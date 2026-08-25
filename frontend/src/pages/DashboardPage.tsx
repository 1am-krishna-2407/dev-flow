import { useEffect, useState } from 'react';
import { fetchAnalytics, fetchProjects, fetchTasks } from '../api';
import { useAuth } from '../state/AuthContext';
import type { AnalyticsData, Project, Task } from '../types';

export default function DashboardPage() {
  const { token, user } = useAuth();
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [projects, setProjects] = useState<Project[]>([]);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!token) return;

    const loadDashboard = async () => {
      setLoading(true);
      setError('');
      try {
        const [analyticsData, projectData, taskData] = await Promise.all([
          fetchAnalytics(token),
          fetchProjects(token),
          fetchTasks(token),
        ]);
        setAnalytics(analyticsData);
        setProjects(projectData);
        setTasks(taskData);
      } catch (err) {
        setError(String(err));
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, [token]);

  return (
    <div className="space-y-8">
      <section className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 shadow-xl shadow-slate-950/20">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm uppercase tracking-[0.35em] text-emerald-400">Howdy, {user?.name ?? 'there'}</p>
            <h1 className="mt-3 text-4xl font-semibold text-white">Your workflow dashboard</h1>
            <p className="mt-3 max-w-2xl text-slate-400">Everything you need to stay in sync with your team is here. Projects, tasks, and analytics all in one place.</p>
          </div>
          <div className="rounded-3xl bg-slate-950/90 p-5 text-center">
            <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Role</p>
            <p className="mt-3 text-2xl font-semibold text-white">{user?.role ?? 'Contributor'}</p>
          </div>
        </div>
      </section>

      {error ? <div className="rounded-3xl border border-rose-500/20 bg-rose-500/10 p-5 text-sm text-rose-100">{error}</div> : null}

      <section className="grid gap-6 xl:grid-cols-4">
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-6">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Projects</p>
          <p className="mt-4 text-4xl font-semibold text-white">{analytics?.projectCount ?? '—'}</p>
          <p className="mt-2 text-sm text-slate-500">Active projects across your account.</p>
        </div>
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-6">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Tasks</p>
          <p className="mt-4 text-4xl font-semibold text-white">{analytics?.taskCount ?? '—'}</p>
          <p className="mt-2 text-sm text-slate-500">Total tasks assigned to your team.</p>
        </div>
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-6">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Team size</p>
          <p className="mt-4 text-4xl font-semibold text-white">{analytics?.userCount ?? '—'}</p>
          <p className="mt-2 text-sm text-slate-500">Members contributing to your workflow.</p>
        </div>
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-6">
          <p className="text-sm uppercase tracking-[0.35em] text-emerald-400">Done</p>
          <p className="mt-4 text-4xl font-semibold text-white">{analytics?.completedTasks ?? '—'}</p>
          <p className="mt-2 text-sm text-slate-500">Tasks successfully completed so far.</p>
        </div>
      </section>

      <section className="grid gap-6 xl:grid-cols-[2fr_1fr]">
        <div className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 shadow-xl shadow-slate-950/10">
          <div className="flex items-center justify-between gap-4">
            <div>
              <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Project snapshot</p>
              <h2 className="mt-2 text-2xl font-semibold text-white">Recent activity</h2>
            </div>
            <span className="rounded-full bg-slate-800 px-4 py-2 text-xs uppercase tracking-[0.35em] text-slate-400">{projects.length} projects</span>
          </div>

          <div className="mt-8 space-y-4">
            {projects.length > 0 ? (
              projects.slice(0, 3).map((project) => (
                <article key={project.id} className="rounded-3xl border border-slate-800 bg-slate-950/90 p-5">
                  <p className="text-sm text-slate-400">{project.status ?? 'Planning'}</p>
                  <h3 className="mt-2 text-xl font-semibold text-white">{project.name}</h3>
                  <p className="mt-2 text-sm text-slate-400">{project.description ?? 'No description added yet.'}</p>
                </article>
              ))
            ) : (
              <p className="text-slate-400">There are no active projects to display yet.</p>
            )}
          </div>
        </div>

        <div className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 shadow-xl shadow-slate-950/10">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Latest tasks</p>
          <h2 className="mt-2 text-2xl font-semibold text-white">In progress</h2>
          <div className="mt-6 space-y-4">
            {tasks.length > 0 ? (
              tasks.slice(0, 4).map((task) => (
                <div key={task.id} className="rounded-3xl border border-slate-800 bg-slate-950/90 p-4">
                  <p className="text-sm text-slate-400">{task.status ?? 'Open'}</p>
                  <h3 className="mt-2 text-lg font-semibold text-white">{task.title}</h3>
                  <p className="mt-1 text-sm text-slate-400">{task.project?.name ?? 'Unknown project'}</p>
                </div>
              ))
            ) : (
              <p className="text-slate-400">No task updates available yet.</p>
            )}
          </div>
        </div>
      </section>

      {loading ? <p className="text-slate-400">Loading dashboard details…</p> : null}
    </div>
  );
}
