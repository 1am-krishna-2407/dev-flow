import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../state/AuthContext';

export default function RegisterPage() {
  const { register, loading } = useAuth();
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');

    if (password.length < 8) {
      setError('Password must be at least 8 characters long.');
      return;
    }

    try {
      await register({ name, email, password });
      navigate('/');
    } catch (err) {
      setError('Unable to register. Please try again with a different email.');
    }
  };

  return (
    <div className="mx-auto my-12 max-w-xl rounded-[2rem] border border-slate-800 bg-slate-950/90 p-10 shadow-2xl shadow-slate-950/30">
      <div className="mb-8 text-center">
        <p className="text-sm uppercase tracking-[0.4em] text-emerald-400">Create account</p>
        <h1 className="mt-4 text-4xl font-semibold text-white">Join DevFlow</h1>
        <p className="mt-3 text-slate-400">Quick sign-up helps you start tracking tasks and staying aligned with your team.</p>
      </div>

      <form className="space-y-6" onSubmit={handleSubmit}>
        <div>
          <label className="block text-sm font-medium text-slate-300">Full name</label>
          <input
            type="text"
            value={name}
            onChange={(event) => setName(event.target.value)}
            required
            className="mt-3 w-full rounded-3xl border border-slate-800 bg-slate-900/90 px-4 py-3 text-white outline-none transition focus:border-emerald-500"
            placeholder="Alex Morgan"
          />
        </div>

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
            placeholder="At least 8 characters"
          />
        </div>

        {error ? <p className="text-sm text-rose-500">{error}</p> : null}

        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-3xl bg-emerald-500 px-5 py-3 text-sm font-semibold text-slate-950 transition hover:bg-emerald-400 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {loading ? 'Creating account…' : 'Create account'}
        </button>
      </form>

      <div className="mt-8 border-t border-slate-800 pt-6 text-center text-sm text-slate-400">
        Already have an account? <Link to="/login" className="text-emerald-400 hover:text-emerald-300">Sign in</Link>
      </div>
    </div>
  );
}
