// Automatically use the current host for API requests, making it cloud-ready
const BASE_URL = window.location.origin + "/api";

// --- Theme Management ---
function initTheme() {
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark" || (!savedTheme && window.matchMedia("(prefers-color-scheme: dark)").matches)) {
        document.documentElement.setAttribute("data-theme", "dark");
    } else {
        document.documentElement.removeAttribute("data-theme");
    }
}
initTheme();

function toggleTheme() {
    if (document.documentElement.getAttribute("data-theme") === "dark") {
        document.documentElement.removeAttribute("data-theme");
        localStorage.setItem("theme", "light");
    } else {
        document.documentElement.setAttribute("data-theme", "dark");
        localStorage.setItem("theme", "dark");
    }
}
// ------------------------


// Auto route check: if on a protected page, verify we have a token
const currentPath = window.location.pathname;
if (!currentPath.endsWith("login.html")) {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        window.location.href = "login.html";
    }
}

// Log Out function
function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

// Global API Fetch helper supporting token refresh and 401 handling
async function apiFetch(endpoint, options = {}) {
    let token = localStorage.getItem("accessToken");
    
    // Setup headers
    if (!options.headers) {
        options.headers = {};
    }
    if (token) {
        options.headers["Authorization"] = `Bearer ${token}`;
    }
    if (!options.headers["Content-Type"] && !(options.body instanceof FormData)) {
        options.headers["Content-Type"] = "application/json";
    }

    let url = endpoint.startsWith("http") ? endpoint : `${BASE_URL}${endpoint}`;
    let response = await fetch(url, options);

    // If 401 Unauthorized, try to refresh the token
    if (response.status === 401) {
        const refreshToken = localStorage.getItem("refreshToken");
        if (refreshToken) {
            try {
                const refreshRes = await fetch(`${BASE_URL}/auth/refresh`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ refreshToken })
                });

                const refreshResult = await refreshRes.json();
                if (refreshRes.ok && refreshResult.success) {
                    // Update access token
                    const newAccessToken = refreshResult.data.accessToken;
                    localStorage.setItem("accessToken", newAccessToken);
                    
                    // Retry original request with new token
                    options.headers["Authorization"] = `Bearer ${newAccessToken}`;
                    response = await fetch(url, options);
                    return response;
                }
            } catch (err) {
                console.error("Token refresh failed:", err);
            }
        }
        
        // If refresh fails or no refresh token, log out
        logout();
    }

    return response;
}
