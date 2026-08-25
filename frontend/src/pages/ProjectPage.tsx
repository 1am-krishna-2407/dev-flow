import { FormEvent, useEffect, useState } from 'react';
import { createProject, fetchProjects } from '../api';
import { useAuth } from '../state/AuthContext';
import type { Project, ProjectStatus } from '../types';

const PROJECT_STATUSES: ProjectStatus[] = ['PLANNING', 'ACTIVE', 'COMPLETED'];

export default function ProjectPage() {
  const { token, user } = useAuth();
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [creating, setCreating] = useState(false);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState<ProjectStatus>('PLANNING');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const canCreateProject = user?.role === 'ADMIN' || user?.role === 'PROJECT_MANAGER';

  useEffect(() => {
    if (!token) return;

    const loadProjects = async () => {
      setLoading(true);
      setError('');
      try {
        const projectData = await fetchProjects(token);
        setProjects(projectData);
      } catch (err) {
        setError(String(err));
      } finally {
        setLoading(false);
      }
    };

    loadProjects();
  }, [token]);

  const handleCreate = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!token) return;

    setCreating(true);
    setError('');

    try {
      const project = await createProject(
        {
          name,
          description,
          status,
          startDate: startDate || undefined,
          endDate: endDate || undefined,
        },
        token,
      );
      setProjects((current) => [project, ...current]);
      setName('');
      setDescription('');
      setStatus('PLANNING');
      setStartDate('');
      setEndDate('');
    } catch (err) {
      setError(String(err));
    } finally {
      setCreating(false);
    }
  };

  return (
    <div className="space-y-8">
      <header className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 shadow-xl shadow-slate-950/20">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm uppercase tracking-[0.35em] text-emerald-400">Projects</p>
            <h1 className="mt-3 text-4xl font-semibold text-white">Project roadmap</h1>
            <p className="mt-3 text-slate-400">Browse active initiatives and stay aligned with your team’s priorities.</p>
          </div>
          {canCreateProject ? (
            <span className="rounded-3xl bg-emerald-500/10 px-4 py-3 text-sm font-medium text-emerald-300">Role: {user.role}</span>
          ) : (
            <span className="rounded-3xl bg-slate-800 px-4 py-3 text-sm font-medium text-slate-300">Role: {user?.role ?? 'Unknown'}</span>
          )}
        </div>
      </header>

      {canCreateProject ? (
        <section className="rounded-3xl border border-slate-800 bg-slate-950/90 p-8 shadow-xl shadow-slate-950/20">
          <h2 className="text-2xl font-semibold text-white">Create a new project</h2>
          <p className="mt-2 text-sm text-slate-400">Add a project plan and keep the team synced.</p>

          <form className="mt-6 grid gap-5 sm:grid-cols-2" onSubmit={handleCreate}>
            <label className="space-y-2 text-sm text-slate-200">
              <span>Name</span>
              <input
                value={name}
                onChange={(event) => setName(event.target.value)}
                required
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
                placeholder="Project name"
              />
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>Status</span>
              <select
                value={status}
                onChange={(event) => setStatus(event.target.value as ProjectStatus)}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              >
                {PROJECT_STATUSES.map((option) => (
                  <option key={option} value={option}>{option.replace('_', ' ')}</option>
                ))}
              </select>
            </label>

            <label className="space-y-2 text-sm text-slate-200 sm:col-span-2">
              <span>Description</span>
              <textarea
                value={description}
                onChange={(event) => setDescription(event.target.value)}
                rows={3}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
                placeholder="What is this project about?"
              />
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>Start date</span>
              <input
                type="date"
                value={startDate}
                onChange={(event) => setStartDate(event.target.value)}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              />
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>End date</span>
              <input
                type="date"
                value={endDate}
                onChange={(event) => setEndDate(event.target.value)}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              />
            </label>

            <button
              type="submit"
              disabled={creating}
              className="sm:col-span-2 inline-flex items-center justify-center rounded-3xl bg-emerald-500 px-6 py-3 text-sm font-semibold text-slate-950 transition hover:bg-emerald-400 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {creating ? 'Creating project…' : 'Create project'}
            </button>
          </form>
        </section>
      ) : null}

      {error ? <div className="rounded-3xl border border-rose-500/20 bg-rose-500/10 p-5 text-sm text-rose-100">{error}</div> : null}

      <div className="grid gap-6 lg:grid-cols-2">
        {projects.length > 0 ? (
          projects.map((project) => (
            <article key={project.id} className="rounded-3xl border border-slate-800 bg-slate-950/90 p-6 shadow-xl shadow-slate-950/10">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="text-sm uppercase tracking-[0.35em] text-slate-400">{project.status ?? 'Planning'}</p>
                  <h2 className="mt-2 text-2xl font-semibold text-white">{project.name}</h2>
                </div>
                <span className="rounded-full bg-slate-800 px-3 py-1 text-xs uppercase tracking-[0.35em] text-slate-400">Project</span>
              </div>
              <p className="mt-4 text-slate-400">{project.description ?? 'No description provided yet.'}</p>
              <div className="mt-5 grid gap-3 sm:grid-cols-2">
                <div className="rounded-3xl bg-slate-900/90 p-4 text-sm text-slate-300">
                  <p className="text-slate-500">Start</p>
                  <p className="mt-2 font-medium text-white">{project.startDate ?? 'TBD'}</p>
                </div>
                <div className="rounded-3xl bg-slate-900/90 p-4 text-sm text-slate-300">
                  <p className="text-slate-500">Deadline</p>
                  <p className="mt-2 font-medium text-white">{project.endDate ?? 'TBD'}</p>
                </div>
              </div>
            </article>
          ))
        ) : (
          <div className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 text-slate-400 shadow-xl shadow-slate-950/10">
            <p className="text-lg">No projects found.</p>
            <p className="mt-2 text-sm text-slate-500">Ask your team to add projects through the backend, or use seeded data to get started.</p>
          </div>
        )}
      </div>

      {loading ? <p className="text-slate-400">Loading projects…</p> : null}
    </div>
  );
}
