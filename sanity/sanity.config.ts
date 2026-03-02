import {defineConfig} from 'sanity'
import {schemaTypes} from './schemas/schema'
import {visionTool} from '@sanity/vision'
import {NavLogo} from './components/NavLogo'
import {structureTool} from 'sanity/structure'
import {DATASET, PROJECT_ID} from './constants'


const auth = {
    redirectOnSingle: true,
    providers: () => [
        {
            name: 'saml',
            title: 'NAV SSO',
            url: 'https://api.sanity.io/v2021-10-01/auth/saml/login/f3270b37',
            logo: '/static/navlogo.png',
        },
    ],
    loginMethod: 'dual',
};

export default defineConfig({
    name: 'production',
    title: 'Prod',
    projectId: PROJECT_ID,
    dataset: DATASET,
    plugins: [
        structureTool({}), visionTool(),
    ],
    tools: (prev) => {
        // 👇 Uses environment variables set by Vite in development mode
        if (import.meta.env.DEV) {
            return prev
        }
        return prev.filter((tool) => tool.name !== 'vision')
    },
    schema: {
        types: schemaTypes,
    },
    auth: auth,
    studio: {
        components: {
            logo: NavLogo
        }
    }
})
