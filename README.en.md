# Smart Travel · Smart Tourism System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-42b883)](https://v3.vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479a1)](https://www.mysql.com/)

A smart tourism platform built with a **Spring Boot 3 + Vue 3** front-end/back-end separated architecture. It offers tourists scenic-spot browsing, ticket booking, order management and reviews, while giving operators an admin console and an ECharts-powered data dashboard. The full Software Requirements Specification is available in [需求文档.md](需求文档.md) (Chinese).

## ✨ Features

**Tourist portal**
- Scenic spot exploration: list search & detail pages (tickets, gallery, ratings)
- Ticket booking with unified order management in the user center
- Profile management with avatar upload
- Spot ratings and reviews

**Admin console**
- Scenic spot CRUD with image upload
- User management
- Real-time ECharts dashboard for orders & visitor traffic

**Planned** (see the SRS): Baidu Maps GIS routing, O2O QR-code ticket verification, RBAC roles (tourist / verifier / admin).

## 🏗️ Repository Layout

```text
travel
├── smart-travel/   # Backend — Spring Boot 3.2.5 + MyBatis-Plus + MySQL 8
├── travel-web/     # Frontend — Vue 3 + Vite 5 + Element Plus
├── studyit/        # Java practice sandbox
└── 需求文档.md      # Requirements Specification (SRS, Chinese)
```

## 🚀 Getting Started

**Prerequisites**: JDK 17+, Node.js 18+, MySQL 8.0.

```bash
# 1. Create the database (schema outlined in the SRS, section 6)
CREATE DATABASE smart_travel DEFAULT CHARACTER SET utf8mb4;

# 2. Start the backend on http://localhost:8080
cd smart-travel
./mvnw spring-boot:run

# 3. Start the frontend
cd travel-web
npm install
npm run dev
```

> The frontend currently hardcodes the API base URL `http://localhost:8080` — start the backend first for local development.

## 📖 Documentation

- [需求文档.md](需求文档.md) — background, architecture, use cases, functional requirements, database design and non-functional requirements (Chinese).

---

© 2026 [miracleboomq-netizen](https://github.com/miracleboomq-netizen)
