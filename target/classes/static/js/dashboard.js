const token = localStorage.getItem("token");

// 🧭 Redirect to login if no token
if (!token) {
  window.location.href = "/login.html";
}

// DOM Elements
const usernameEl = document.getElementById("username");
const emailEl = document.getElementById("email");
const welcomeUser = document.getElementById("welcome-user");

// ✅ Show only one section at a time
function showSection(id) {
  document.getElementById("welcome-full").style.display = "none"; // Hide big welcome
  document.getElementById("mini-welcome").classList.remove("hidden"); // Show small welcome in header

  document.querySelectorAll(".content-section").forEach(section => {
    section.style.display = "none";
  });

  document.getElementById(`${id}-section`).style.display = "block";

  if (id === "notes") loadNotes();
  if (id === "profile") loadProfile();
}
document.addEventListener("DOMContentLoaded", () => {
  const username = localStorage.getItem("username") || "User";
  document.getElementById("welcome-user").innerText = `Welcome, ${username}!`;
  document.getElementById("welcome-name-small").innerText = username;
});

// 🧠 Load user profile
function loadProfile() {
  fetch("/api/users/me", {
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
    .then(res => res.json())
    .then(user => {
      usernameEl.innerText = user.username;
      emailEl.innerText = user.email;
      welcomeUser.innerText = `Welcome, ${user.username}!`;
    })
    .catch(() => {
      alert("Error loading profile");
    });
}

// 📋 Load notes
function loadNotes() {
  const list = document.getElementById("note-list");
  list.innerHTML = "";
  showLoader(true);
  fetch("/api/notes", {
    headers: {
      Authorization: `Bearer ${token}`
    }
  })
    .then(res => res.json())
    .then(notes => {
      showLoader(false);
      notes.forEach(note => {
        const card = document.createElement("div");
        card.className = "note-card";
        card.innerHTML = `
          <strong>${note.title}</strong>
          <p>${note.content}</p>
          <button onclick="editNote(${note.id}, '${note.title}', '${note.content}')">Edit</button>
          <button onclick="deleteNote(${note.id})">Delete</button>
        `;
        list.appendChild(card);
      });
    });
}


// ➕ Create new note
function createNote() {
  const title = document.getElementById("note-title").value;
  const content = document.getElementById("note-content").value;

  if (!title || !content) {
    showToast("Please enter title and content");
    return;
  }

  showLoader(true);
  fetch("/api/notes", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ title, content })
  })
    .then(res => {
      showLoader(false);
      if (res.ok) {
        showToast("Note created!");
        document.getElementById("note-title").value = "";
        document.getElementById("note-content").value = "";
        showSection("notes");
      } else {
        alert("Error creating note");
      }
    });
}

// 🗑️ Delete a note
function deleteNote(id) {
  if (!confirm("Delete this note?")) return;

  fetch(`/api/notes/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`
    }
  }).then(() => {
    showToast("Note deleted");
    loadNotes();
  });
}

// 📝 Edit a note
function editNote(id, oldTitle, oldContent) {
  const newTitle = prompt("Edit title:", oldTitle);
  const newContent = prompt("Edit content:", oldContent);

  if (newTitle && newContent) {
    fetch(`/api/notes/${id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ title: newTitle, content: newContent })
    }).then(() => {
      showToast("Note updated");
      loadNotes();
    });
  }
}

// 🚪 Logout
function logout() {
  localStorage.removeItem("token");
  window.location.href = "/login.html";
}

// 🔔 Toast & Loader
function showToast(msg) {
  const toast = document.getElementById("toast");
  toast.innerText = msg;
  toast.style.display = "block";
  setTimeout(() => { toast.style.display = "none"; }, 3000);
}

function showLoader(show) {
  document.getElementById("loader").style.display = show ? "block" : "none";
}
document.addEventListener("DOMContentLoaded", () => {
  const username = localStorage.getItem("username") || "User";
  document.getElementById("username-big").innerText = `Welcome, ${username}!`;
  document.getElementById("username-mini").innerText = username;
});

function showSection(sectionId) {
  // Hide welcome fullscreen, show mini-welcome
  document.getElementById("welcome-full").classList.add("hidden");
  document.getElementById("mini-welcome").classList.remove("hidden");

  // Hide all sections
  document.querySelectorAll(".content").forEach(section => {
    section.classList.add("hidden");
  });

  // Show requested section
  document.getElementById(`${sectionId}-section`).classList.remove("hidden");

  // Load notes/profile if needed
  if (sectionId === "notes") loadNotes();
  if (sectionId === "profile") loadProfile();
}

// ✅ Start app
loadProfile();
showSection("notes");
