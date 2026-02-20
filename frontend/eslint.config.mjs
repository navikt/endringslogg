// @ts-check

import eslint from '@eslint/js';
import configPrettier from 'eslint-config-prettier';
import pluginReactHooks from 'eslint-plugin-react-hooks';
import tseslint from 'typescript-eslint';

export default tseslint.config([
  { ignores: ['dist'] },
  eslint.configs.recommended,
  tseslint.configs.recommended,
  pluginReactHooks.configs['recommended-latest'],
  configPrettier,
  {
    rules: {
      '@typescript-eslint/no-unused-vars': [
        'error',
        { argsIgnorePattern: '^_', varsIgnorePattern: '^_' },
      ],
    },
  },
]);
