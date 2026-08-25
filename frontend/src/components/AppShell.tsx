import { NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../state/AuthContext';

const links = [
  { label: 'Dashboard', path: '/' },
  { label: 'Projects', path: '/projects' },
  { label: 'Tasks', path: '/tasks' },
  { label: 'Analytics', path: '/analytics' },
];

export default function AppShell() {
  const { user, logout } = useAuth();

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <div className="lg:grid lg:grid-cols-[280px_1fr]">
        <aside className="border-r border-slate-800 bg-slate-950 p-6">
          <div className="mb-10">
            <p className="text-sm uppercase tracking-[0.35em] text-slate-500">DevFlow</p>
            <h1 className="mt-4 text-3xl font-semibold text-white">Workflow Hub</h1>
            <p className="mt-2 text-sm text-slate-400">Simple project and task management for your team.</p>
          </div>

          <nav className="space-y-2">
            {links.map((link) => (
              <NavLink
                key={link.path}
                to={link.path}
                end={link.path === '/'}
                className={({ isActive }) =>
                  `block rounded-3xl px-4 py-3 text-sm transition ${
                    isActive ? 'bg-emerald-500 text-slate-950' : 'text-slate-200 hover:bg-slate-900/80'
                  }`
                }
              >
                {link.label}
              </NavLink>
            ))}
          </nav>

          <div className="mt-12 rounded-3xl bg-slate-900/90 p-5 text-sm text-slate-300">
            <p className="font-medium text-slate-100">Signed in as</p>
            <p className="mt-2 text-base font-semibold">{user?.name || 'Team member'}</p>
            <p className="text-slate-500">{user?.designation || 'Collaborator'}</p>
            <button
              onClick={logout}
              className="mt-5 w-full rounded-3xl border border-slate-700 bg-slate-950/90 px-4 py-3 text-sm text-slate-100 transition hover:border-slate-500"
            >
              Sign out
            </button>
          </div>
        </aside>

        <main className="p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
