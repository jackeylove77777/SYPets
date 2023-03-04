<template>
  <div id="app" style="display: none;" :style="{display: 'block'}">
    <keep-alive exclude="Category"><router-view /></keep-alive>
  </div>
</template>