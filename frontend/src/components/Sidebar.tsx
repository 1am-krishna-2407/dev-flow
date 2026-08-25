import React from 'react';

const links = ['Dashboard', 'Projects', 'Tasks', 'Repositories', 'Analytics', 'Notifications', 'Settings'];

export default function Sidebar() {
  return (
    <aside className="flex min-h-screen w-64 flex-col gap-4 border-r border-slate-800 bg-slate-950 p-6 text-slate-100">
      <div className="mb-8">
        <p className="text-sm uppercase tracking-[0.35em] text-slate-500">DevFlow</p>
        <h2 className="mt-4 text-2xl font-semibold">Command Center</h2>
      </div>
      <nav className="space-y-2 text-sm">
        {links.map((link) => (
          <button key={link} className="block w-full rounded-3xl px-4 py-3 text-left transition hover:bg-slate-900/80">
            {link}
          </button>
        ))}
      </nav>
    </aside>
  );
}
