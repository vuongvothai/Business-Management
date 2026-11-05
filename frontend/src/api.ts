export async function apiFetch(input: RequestInfo | URL, init: RequestInit = {}) {
  const token = sessionStorage.getItem('accessToken')
  const headers = new Headers(init.headers)
  if (token) headers.set('Authorization', `Bearer ${token}`)

  const response = await fetch(input, { ...init, headers })
  if (response.status === 401) {
    sessionStorage.removeItem('accessToken')
    sessionStorage.removeItem('authUser')
    window.dispatchEvent(new Event('auth-expired'))
  }
  return response
}
