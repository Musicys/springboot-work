<template>
   <iframe
      :onload="onloadCode"
      style="
         width: 100%;
         height: 100%;
         border: 1px solid #fff;
         background: #000;
      "></iframe>
</template>

<script lang="ts" setup>
const props = defineProps({
   videoUrl: String,
   poster: String
});
let onloadCode = `
const url = '${props.videoUrl}'  
const poster = '${props.poster}'
 this.contentWindow.document.body.innerHTML = '<video style="width: 100%;height: 100%" controls="controls" poster="'+poster+'" src="'+url+'" ></video>';  
`;
</script>
