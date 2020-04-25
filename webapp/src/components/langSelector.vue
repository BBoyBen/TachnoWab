<template>
  <div class="lang-selector">
    <v-select
      v-model="$i18n.locale"
      :items="langs"
      @change="change"
      prepend-icon="language"
      solo
    >
      <template v-slot:selection="data">
        {{ $t(data.item.resKey) }}
      </template>
      <template v-slot:item="data">
        {{ $t(data.item.resKey) }}
      </template>
    </v-select>
  </div>
</template>

<script>
import { EventBus } from "../event-bus";

export default {
  name: "lang-selector",
  data: () => ({
    langs: [
      {
        resKey: "lang.fr",
        value: "fr"
      },
      {
        resKey: "lang.en",
        value: "en"
      }
    ]
  }),
  methods: {
    change() {
      EventBus.$emit("langChange");
    }
  }
};
</script>

<style scoped>
.lang-selector {
  position: absolute;
  right: 15px;
  top: 7px;
  width: 150px;
  height: 50px;
}
</style>
