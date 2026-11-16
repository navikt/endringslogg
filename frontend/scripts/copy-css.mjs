import { cpSync, statSync } from 'node:fs';

cpSync('src', 'dist', {
    recursive: true,
    filter: path => path.endsWith('.css') || statSync(path).isDirectory(),
});
