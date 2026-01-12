<template>
  <div class="bg-gradient-to-r from-danger to-orange-500 rounded-xl shadow-lg p-5 mb-6 text-white">
    <h2 class="text-lg font-semibold mb-4 flex items-center">
      <i class="fa fa-exclamation-triangle mr-2"></i> 紧急联系电话
    </h2>
    <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
      <div class="bg-white/20 backdrop-blur-sm rounded-lg p-3 text-center">
        <div class="text-2xl font-bold mb-1">119</div>
        <div class="text-sm opacity-90">消防救援</div>
      </div>
      <div class="bg-white/20 backdrop-blur-sm rounded-lg p-3 text-center">
        <div class="text-2xl font-bold mb-1">120</div>
        <div class="text-sm opacity-90">医疗急救</div>
      </div>
      <div class="bg-white/20 backdrop-blur-sm rounded-lg p-3 text-center">
        <div class="text-2xl font-bold mb-1">110</div>
        <div class="text-sm opacity-90">报警电话</div>
      </div>
      <div class="bg-white/20 backdrop-blur-sm rounded-lg p-3 text-center">
        <div class="text-xl font-bold mb-1">0791-88669999</div>
        <div class="text-sm opacity-90">园区安保</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EmergencyContactCard'
}
</script>