import {createRouter, createWebHistory} from 'vue-router'
import Layout from "@/layout/Layout";

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/home",
        children: [
            {path: 'home', name: 'Home', component: () => import("@/views/Home")},
            {path: 'videoStore', name: 'VideoStore', component: () => import("@/views/VideoStore")},
            {
                path: '/userInformation', name: 'UserInformation',redirect:'/modifyUserInformation', component: () => import("@/views/userInformation/UserInformation"), children: [
                    {path: '/modifyUserInformation', name: 'ModifyUserInformation', component: () => import("@/views/userInformation/ModifyUserInformation")},
                    {path: '/modifyUserPassword', name: 'ModifyUserPassword', component: () => import("@/views/userInformation/ModifyUserPassword")},
                    {path: '/modifyUserIcon', name: 'ModifyUserIcon', component: () => import("@/views/userInformation/ModifyUserIcon")},
                ]
            },
            {path: 'videoUpload', name: 'VideoUpload', component: () => import("@/views/VideoUpload")},
            {path: 'blog', name: 'Blog', component: () => import("@/views/Blog")},
            {path: 'video-list-album/:id', name: 'Video-list-album', component: () => import("@/views/VideoPlayer")},
            {path: 'login', name: 'Login', component: () => import("@/views/Login")},
            {path: 'register', name: 'Register', component: () => import("@/views/Register")},
            {path: 'search/:key', name: 'Search', component: () => import("@/views/Search")},
            {
                path: '/adminManager', name: 'AdminManager',redirect: '/adminVideo',component: () =>import("@/views/administratorManager/adminManager"),children: [
                    {path: '/adminVideo', name: 'AdminVideo', component: () => import("@/views/administratorManager/adminVideo")},
                    {path: '/adminUser', name: 'AdminUser', component: () => import("@/views/administratorManager/adminUser")},
                    {path: '/adminType', name: 'AdminType', component: () => import("@/views/administratorManager/adminType")},
                    {path: '/adminArticleType', name: 'AdminArticleType', component: () => import("@/views/administratorManager/adminArticleType")},
                    {path: '/adminBlog', name: 'AdminBlog', component: () => import("@/views/administratorManager/adminBlog")},
                ]
            },
            {path: 'vueCropper2', name: 'VueCropper2', component: () => import("@/views/administratorManager/cropper/VueCropper2")},
            {path: 'tailoring', name: 'Tailoring', component: () => import("@/views/administratorManager/cropper/Tailoring")},
        ]
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
