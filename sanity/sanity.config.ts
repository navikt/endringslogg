import {defineConfig} from 'sanity'
import {schemaTypes} from './schemas/schema'
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
    loginMethod: 'dual' as const,
};

export default defineConfig({
    name: 'production',
    title: 'Prod',
    projectId: PROJECT_ID,
    dataset: DATASET,
    plugins: [structureTool({})],
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
