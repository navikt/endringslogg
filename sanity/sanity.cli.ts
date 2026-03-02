import {DATASET, PROJECT_ID} from './constants'
import {defineCliConfig} from 'sanity/cli'


export default defineCliConfig({
    api: {
        projectId: PROJECT_ID,
        dataset: DATASET
    },
});
