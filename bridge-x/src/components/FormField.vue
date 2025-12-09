<template>
  <div class="input-row">
    <input
      :value="modelValue"
      :type="type"
      :placeholder="placeholder"
      class="input-field"
      v-bind="$attrs"
      @input="$emit('update:modelValue', $event.target.value)"
    />
    <div v-if="error" class="error-text">{{ error }}</div>
  </div>
</template>

<script setup>
defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  },
  placeholder: {
    type: String,
    default: ''
  },
  error: {
    type: String,
    default: ''
  }
});

defineEmits(['update:modelValue']);
</script>

<style lang="scss" scoped>
@use "sass:color";

$color-border: #ccc;
$color-text-placeholder: #888;
$color-error: #e74c3c;
$spacing-xl: 20px;
$radius-md: 5px;
$font-size-sm: 0.9rem;
$font-size-base: 1rem;

.input-row {
  width: 100%;
  margin-bottom: $spacing-xl;

  &:last-child {
    margin-bottom: 0;
  }
}

.input-field {
  width: 100%;
  padding: 15px 10px;
  border: 1px solid $color-border;
  border-radius: $radius-md;
  box-sizing: border-box;
  font-size: $font-size-base;
  margin-bottom: 0;

  &::placeholder {
    color: $color-text-placeholder;
  }

  &:focus {
    outline: none;
    border-color: color.adjust($color-border, $lightness: -10%);
  }
}

.error-text {
  color: $color-error;
  padding: 5px 5px;
  font-size: $font-size-sm;
  margin-top: 5px;
}
</style>
