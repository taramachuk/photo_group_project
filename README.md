

# Photo Group Project

**Photo Group Project** is a full‑stack web application built with **Java**, **React**, and **Supabase** that allows photographers to discover, share, and explore amazing photography spots. It combines interactive mapping, rich media, and social discovery into a Pinterest-like experience for photo locations.

---

## 🌍 Features

* **📍 Location Sharing:** Pin and describe beautiful photo spots on an interactive map.
* **🔎 Visual Discovery:** Browse and search shared locations in a Pinterest-style feed.
* **📸 Community‑Driven:** Users contribute, rate, and comment on spots.
* **🗺️ Map Integration:** Easily find spots near you or plan a photography route.
* **🛠️ Full‑Stack Tech:** React frontend, Java backend, Supabase for database and authentication.

---

## 🧩 Tech Stack

| Layer                     | Technology                                   |
| ------------------------- | -------------------------------------------- |
| Frontend                  | React                                        |
| Backend                   | Java                                         |
| Database/Backend Services | Supabase (Authentication, Storage, Realtime) |
| Styling                   | CSS / Tailwind (if applicable)               |

---

## 🚀 Getting Started

Follow these steps to set up the project locally:

### Prerequisites

* [Node.js](https://nodejs.org/) >= 18
* [Java JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) >= 17
* [Supabase Account](https://supabase.com/)
* [Git](https://git-scm.com/)

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/taramachuk/photo_group_project.git
cd photo_group_project
```

### 2️⃣ Setup Backend (Java)

* Navigate to the backend folder:

```bash
cd backend
```

* Install dependencies (if using Maven):

```bash
mvn clean install
```

* Configure Supabase credentials in `application.properties` or `.env` (endpoint, API key)
* Run the server:

```bash
mvn spring-boot:run
```

### 3️⃣ Setup Frontend (React)

* Navigate to the frontend folder:

```bash
cd ../frontend
```

* Install dependencies:

```bash
npm install
```

* Add Supabase environment variables in `.env.local`:

```env
REACT_APP_SUPABASE_URL=your_supabase_url
REACT_APP_SUPABASE_KEY=your_supabase_key
```

* Start the frontend:

```bash
npm start
```

Open [http://localhost:3000](http://localhost:3000) in your browser to see the app.

---

## 🛠️ Project Structure

```
photo_group_project/
├─ backend/       # Java backend (API endpoints, database logic)
├─ frontend/      # React frontend (UI, components)
├─ README.md
└─ ...
```

---

## 💡 Usage

1. Create an account or sign in with Supabase auth.
2. Pin your favorite photography spots with a photo, title, and description.
3. Explore other locations through the feed or the interactive map.
4. Save spots for future visits and share inspiration with the community.

---

## ✨ Future Improvements

* User profiles with photo galleries
* Advanced search and filter by tags, distance, or popularity
* Comments and likes for each location
* Mobile app version

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.


