import Reflux from 'reflux';

const RulesActions = Reflux.createActions({
  delete: { asyncResult: true },
  list: { asyncResult: true },
  get: { asyncResult: true },
  save: { asyncResult: true },
  update: { asyncResult: true },
  parse: { asyncResult: true },
  multiple: { asyncResult: true },
  loadFunctions: { asyncResult: true },
});

export default RulesActions;