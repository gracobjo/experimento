import { render, screen, fireEvent } from '@testing-library/react';
import LoginForm from '../components/LoginForm';

test('renders login form and submits', () => {
  render(<LoginForm />);
  expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/contraseña/i)).toBeInTheDocument();
  fireEvent.change(screen.getByLabelText(/email/i), { target: { value: 'test@example.com' } });
  fireEvent.change(screen.getByLabelText(/contraseña/i), { target: { value: 'password123' } });
  fireEvent.click(screen.getByRole('button', { name: /iniciar sesión/i }));
  // Aquí podrías mockear la función de envío y comprobar que se llama
}); 