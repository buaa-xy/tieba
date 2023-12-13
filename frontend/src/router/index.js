import Vue from "vue";
import VueRouter from "vue-router";

const Home = () => import("../views/Home.vue");
const About = () => import("../views/About.vue");
//const About = () => import("../components/cards/CommentCard.vue");
const Explore = () => import("../views/Explore.vue");
const Notifications = () => import("../views/Notifications.vue");
const Follow = () => import("../views/Follow.vue");
const Settings = () => import("../views/Settings.vue");
const Compose = () => import("../views/Compose.vue");
//const Register = () => import("../components/test_login.vue");
const Register = () => import("../views/Register.vue");
const Post = () => import("../views/Post.vue");
const Edit = () => import("../views/Edit.vue");
const User = () => import("../views/User.vue");

Vue.use(VueRouter);

const routes = [
  { path: "/", redirect: "/home" },
  {
    path: "/home",
    name: "Home",
    component: Home,
  },
  {
    path: "/about",
    name: "About",
    component: About,
  },
  {
    path: "/explore",
    name: "Explore",
    component: Explore,
  },
  {
    path: "/notifications",
    name: "Notifications",
    component: Notifications,
    meta: {
      loginRequest: true,
    },
  },
  {
    path: "/follow",
    name: "Follow",
    component: Follow,
    meta: {
      loginRequest: true,
    },
  },
  {
    path: "/settings",
    name: "Settings",
    component: Settings,
    meta: {
      loginRequest: true,
    },
  },
  {
    path: "/compose",
    name: "Compose",
    component: Compose,
    meta: {
      loginRequest: true,
    },
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/post/:pid",
    name: "Post",
    component: Post,
  },
  {
    path: "/edit/:pid",
    name: "Edit",
    component: Edit,
  },
  {
    path: "/user/:userId",
    name: "User",
    component: User,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

// router.beforeEach((to, from, next) => {
//   if (to.meta.loginRequest == true) {
//     let isLogin = sessionStorage.getItem("isLogin");
//     if (isLogin != "true") {
//       next({
//         path: "/home",
//       });
//     } else {
//       next();
//     }
//   } else {
//     next();
//   }
// });

export default router;
