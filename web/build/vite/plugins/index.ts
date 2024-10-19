/**
 * 封装plugins数组统一调用
 */
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import {AutoImportDeps} from './autoImport';
import {ConfigCompressPlugin} from './compress';
import {ConfigRestartPlugin} from './restart';
import {ConfigProgressPlugin} from './progress';
import {ConfigVisualizerConfig} from "./visualizer";

export function createVitePlugins(isBuild: boolean) {
    const vitePlugins = [
        // vue支持
        vue(),
        // JSX支持
        vueJsx(),
    ];

    // 自动按需引入依赖
    vitePlugins.push(AutoImportDeps());

    // 开启.gz压缩  rollup-plugin-gzip
    // @ts-ignore
    vitePlugins.push(ConfigCompressPlugin());

    // 监听配置文件改动重启
    vitePlugins.push(ConfigRestartPlugin());

    // 构建时显示进度条
    // @ts-ignore
    vitePlugins.push(ConfigProgressPlugin());

    // 构建时显示进度条
    // @ts-ignore
    vitePlugins.push(ConfigVisualizerConfig());

    return vitePlugins;
}
