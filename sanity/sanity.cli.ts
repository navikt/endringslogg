import {DATASET, PROJECT_ID} from './constants'
import {defineCliConfig} from 'sanity/cli'


export default defineCliConfig({
    api: {
        projectId: PROJECT_ID,
        dataset: DATASET
    },
    deployment: {
        appId: 'fefd0391c35372f453ce004d',
    },
});
