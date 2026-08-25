import { Link } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <section className="mx-auto my-24 max-w-3xl rounded-3xl border border-slate-800 bg-slate-950/80 p-10 text-center text-slate-200 shadow-xl shadow-slate-950/20">
      <p className="text-sm uppercase tracking-[0.4em] text-emerald-400">Page not found</p>
      <h1 className="mt-6 text-5xl font-semibold">404</h1>
      <p className="mt-4 text-lg text-slate-400">The page you’re looking for doesn’t exist.</p>
      <Link to="/" className="mt-8 inline-flex rounded-3xl bg-emerald-500 px-6 py-3 text-sm font-semibold text-slate-950 transition hover:bg-emerald-400">
        Return to dashboard
      </Link>
    </section>
  );
}
