import React from 'react';

export default function Topbar() {
  return (
    <header className="flex flex-col gap-4 border-b border-slate-800 bg-slate-900/90 p-6 sm:flex-row sm:items-center sm:justify-between">
      <div>
        <p className="text-sm uppercase tracking-[0.4em] text-slate-500">Workspace</p>
        <h1 className="mt-2 text-3xl font-semibold text-slate-50">Overview</h1>
      </div>
      <div className="flex flex-1 items-center gap-3 rounded-3xl border border-slate-800 bg-slate-950/90 px-4 py-3 text-slate-400">
        <span>Search</span>
        <input className="flex-1 bg-transparent outline-none" placeholder="Search projects, tasks, repos..." />
      </div>
    </header>
  );
}
