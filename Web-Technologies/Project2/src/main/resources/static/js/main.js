// Armazena preferências do utilizador no localStorage
document.addEventListener('DOMContentLoaded', () => {
  // Tema (claro/escuro)
  const prefersDark = localStorage.getItem('prefersDark') === 'true';
  if (prefersDark) document.body.classList.add('dark');
});
