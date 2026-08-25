import { useEffect, useState } from 'react';
import { fetchAnalytics } from '../api';
import { useAuth } from '../state/AuthContext';
import type { AnalyticsData } from '../types';

export default function AnalyticsPage() {
  const { token } = useAuth();
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!token) return;

    const loadAnalytics = async () => {
      setLoading(true);
      setError('');
      try {
        const analyticsData = await fetchAnalytics(token);
        setAnalytics(analyticsData);
      } catch (err) {
        setError(String(err));
      } finally {
        setLoading(false);
      }
    };

    loadAnalytics();
  }, [token]);

  return (
    <div className="space-y-8">
      <header className="rounded-3xl border border-slate-800 bg-slate-900/80 p-8 shadow-xl shadow-slate-950/20">
        <p className="text-sm uppercase tracking-[0.35em] text-emerald-400">Analytics</p>
        <h1 className="mt-3 text-4xl font-semibold text-white">Team health</h1>
        <p className="mt-3 text-slate-400">Metrics from your backend to help your crew stay on track.</p>
      </header>

      {error ? <div className="rounded-3xl border border-rose-500/20 bg-rose-500/10 p-5 text-sm text-rose-100">{error}</div> : null}

      <section className="grid gap-6 xl:grid-cols-2">
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-8 shadow-xl shadow-slate-950/20">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Project adoption</p>
          <p className="mt-6 text-5xl font-semibold text-white">{analytics?.projectCount ?? '—'}</p>
          <p className="mt-3 text-slate-400">Projects currently tracked by your team.</p>
        </div>
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-8 shadow-xl shadow-slate-950/20">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Task load</p>
          <p className="mt-6 text-5xl font-semibold text-white">{analytics?.taskCount ?? '—'}</p>
          <p className="mt-3 text-slate-400">Tasks that need attention this sprint.</p>
        </div>
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-8 shadow-xl shadow-slate-950/20">
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">Team size</p>
          <p className="mt-6 text-5xl font-semibold text-white">{analytics?.userCount ?? '—'}</p>
          <p className="mt-3 text-slate-400">Users currently in the system.</p>
        </div>
        <div className="rounded-3xl border border-slate-800 bg-slate-950/90 p-8 shadow-xl shadow-slate-950/20">
          <p className="text-sm uppercase tracking-[0.35em] text-emerald-400">Completion</p>
          <p className="mt-6 text-5xl font-semibold text-white">{analytics?.completedTasks ?? '—'}</p>
          <p className="mt-3 text-slate-400">Tasks completed so far.</p>
        </div>
      </section>

      {loading ? <p className="text-slate-400">Loading metrics…</p> : null}
    </div>
  );
}
