import { FormEvent, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../state/AuthContext';

export default function LoginPage() {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: Location })?.from?.pathname || '/';

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    try {
      await login({ email, password });
      navigate(from, { replace: true });
    } catch (err) {
      setError('Invalid credentials. Please check your email and password.');
    }
  };

  return (
    <div className="mx-auto my-12 max-w-xl rounded-[2rem] border border-slate-800 bg-slate-950/90 p-10 shadow-2xl shadow-slate-950/30">
      <div className="mb-8 text-center">
        <p className="text-sm uppercase tracking-[0.4em] text-emerald-400">Sign in</p>
        <h1 className="mt-4 text-4xl font-semibold text-white">Welcome back</h1>
        <p className="mt-3 text-slate-400">Log in to manage your team’s workflow with simplicity and clarity.</p>
      </div>

      <form className="space-y-6" onSubmit={handleSubmit}>
        <div>
          <label className="block text-sm font-medium text-slate-300">Email</label>
          <input
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
            className="mt-3 w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-white outline-none transition focus:border-emerald-500"
            placeholder="you@example.com"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-300">Password</label>
          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
            className="mt-3 w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-white outline-none transition focus:border-emerald-500"
            placeholder="Enter password"
          />
        </div>

        {error ? <p className="text-sm text-rose-500">{error}</p> : null}

        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-3xl bg-emerald-500 px-5 py-3 text-sm font-semibold text-slate-950 transition hover:bg-emerald-400 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {loading ? 'Signing in…' : 'Sign in'}
        </button>
      </form>

      <div className="mt-8 border-t border-slate-800 pt-6 text-center text-sm text-slate-400">
        New here? <Link to="/register" className="text-emerald-400 hover:text-emerald-300">Create an account</Link>
      </div>
    </div>
  );
}
