import { FormEvent, useEffect, useState } from 'react';
import { createTask, fetchProjects, fetchTasks } from '../api';
import { useAuth } from '../state/AuthContext';
import type { Project, Task, Priority, TaskStatus } from '../types';

const TASK_PRIORITIES: Priority[] = ['LOW', 'MEDIUM', 'HIGH'];
const TASK_STATUSES: TaskStatus[] = ['BACKLOG', 'TODO', 'IN_PROGRESS', 'DONE'];

export default function TaskPage() {
  const { token, user } = useAuth();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [creating, setCreating] = useState(false);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState<Priority>('MEDIUM');
  const [status, setStatus] = useState<TaskStatus>('BACKLOG');
  const [dueDate, setDueDate] = useState('');
  const [projectId, setProjectId] = useState<number | undefined>(undefined);

  const canCreateTask = user?.role === 'ADMIN' || user?.role === 'PROJECT_MANAGER' || user?.role === 'DEVELOPER';

  useEffect(() => {
    if (!token) return;

    const loadData = async () => {
      setLoading(true);
      setError('');
      try {
        const [taskData, projectData] = await Promise.all([fetchTasks(token), fetchProjects(token)]);
        setTasks(taskData);
        setProjects(projectData);
        if (!projectId && projectData.length > 0) {
          setProjectId(projectData[0].id);
        }
      } catch (err) {
        setError(String(err));
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [token]);

  const handleCreate = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!token) return;

    setCreating(true);
    setError('');

    try {
      const task = await createTask(
        {
          title,
          description,
          priority,
          status,
          dueDate: dueDate || undefined,
          projectId,
        },
        token,
      );
      setTasks((current) => [task, ...current]);
      setTitle('');
      setDescription('');
      setPriority('MEDIUM');
      setStatus('BACKLOG');
      setDueDate('');
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
            <p className="text-sm uppercase tracking-[0.35em] text-emerald-400">Tasks</p>
            <h1 className="mt-3 text-4xl font-semibold text-white">Work items</h1>
            <p className="mt-3 text-slate-400">Review what your team is working on and identify the next priorities.</p>
          </div>
          <span className="rounded-3xl bg-slate-800 px-4 py-3 text-sm font-medium text-slate-300">Role: {user?.role ?? 'Unknown'}</span>
        </div>
      </header>

      {canCreateTask ? (
        <section className="rounded-3xl border border-slate-800 bg-slate-950/90 p-8 shadow-xl shadow-slate-950/20">
          <h2 className="text-2xl font-semibold text-white">Create a new task</h2>
          <p className="mt-2 text-sm text-slate-400">Add a work item, assign it to a project, and move your team forward.</p>

          <form className="mt-6 grid gap-5 lg:grid-cols-2" onSubmit={handleCreate}>
            <label className="space-y-2 text-sm text-slate-200">
              <span>Title</span>
              <input
                value={title}
                onChange={(event) => setTitle(event.target.value)}
                required
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
                placeholder="Task title"
              />
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>Project</span>
              <select
                value={projectId}
                onChange={(event) => setProjectId(Number(event.target.value))}
                required
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              >
                {projects.map((project) => (
                  <option key={project.id} value={project.id}>{project.name}</option>
                ))}
              </select>
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>Priority</span>
              <select
                value={priority}
                onChange={(event) => setPriority(event.target.value as Priority)}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              >
                {TASK_PRIORITIES.map((option) => (
                  <option key={option} value={option}>{option}</option>
                ))}
              </select>
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>Status</span>
              <select
                value={status}
                onChange={(event) => setStatus(event.target.value as TaskStatus)}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              >
                {TASK_STATUSES.map((option) => (
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
                placeholder="Describe the task details"
              />
            </label>

            <label className="space-y-2 text-sm text-slate-200">
              <span>Due date</span>
              <input
                type="date"
                value={dueDate}
                onChange={(event) => setDueDate(event.target.value)}
                className="w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-slate-100 outline-none focus:border-emerald-500"
              />
            </label>

            <button
              type="submit"
              disabled={creating}
              className="sm:col-span-2 inline-flex items-center justify-center rounded-3xl bg-emerald-500 px-6 py-3 text-sm font-semibold text-slate-950 transition hover:bg-emerald-400 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {creating ? 'Creating task…' : 'Create task'}
            </button>
          </form>
        </section>
      ) : null}

      {error ? <div className="rounded-3xl border border-rose-500/20 bg-rose-500/10 p-5 text-sm text-rose-100">{error}</div> : null}

      <div className="grid gap-6">
        {tasks.length > 0 ? (
          tasks.map((task) => (
            <article key={task.id} className="rounded-3xl border border-slate-800 bg-slate-950/90 p-6 shadow-xl shadow-slate-950/10">
              <div className="flex flex-col gap-6 lg:flex-row lg:items-start lg:justify-between">
                <div>
                  <p className="text-sm uppercase tracking-[0.35em] text-slate-400">{task.status ?? 'Open'}</p>
                  <h2 className="mt-2 text-2xl font-semibold text-white">{task.title}</h2>
                  <p className="mt-3 text-slate-400">{task.description ?? 'No task description available.'}</p>
                </div>
                <div className="grid gap-3 text-sm sm:grid-cols-2">
                  <div className="rounded-3xl bg-slate-900/90 p-4 text-slate-300">
                    <p className="text-slate-500">Project</p>
                    <p className="mt-2 font-medium text-white">{task.project?.name ?? 'Unknown'}</p>
                  </div>
                  <div className="rounded-3xl bg-slate-900/90 p-4 text-slate-300">
                    <p className="text-slate-500">Priority</p>
                    <p className="mt-2 font-medium text-white">{task.priority ?? 'Normal'}</p>
                  </div>
                </div>
              </div>
            </article>
          ))
        ) : (
          <div className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 text-slate-400 shadow-xl shadow-slate-950/10">
            <p className="text-lg">No tasks available.</p>
            <p className="mt-2 text-sm text-slate-500">New tasks will appear here once your team adds them to the board.</p>
          </div>
        )}
      </div>

      {loading ? <p className="text-slate-400">Loading tasks…</p> : null}
    </div>
  );
}
