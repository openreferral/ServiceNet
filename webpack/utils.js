const path = require('path');
const fs = require('fs');

const tsconfig = require('../tsconfig.json');

module.exports = {
  parseVersion,
  root,
  mapTypescriptAliasToWebpackAlias
};

const _root = path.resolve(__dirname, '..');

function root(args) {
  args = Array.prototype.slice.call(arguments, 0);
  return path.join.apply(path, [_root].concat(args));
}

function mapTypescriptAliasToWebpackAlias(alias = {}) {
  const webpackAliases = { ...alias };
  if (!tsconfig.compilerOptions.paths) {
    return webpackAliases;
  }
  Object.entries(tsconfig.compilerOptions.paths)
    .filter(([key, value]) => {
      // use Typescript alias in Webpack only if this has value
      return !!value.length;
    })
    .map(([key, value]) => {
      // if Typescript alias ends with /* then remove this for Webpack
      const regexToReplace = /\/\*$/;
      const aliasKey = key.replace(regexToReplace, '');
      const aliasValue = value[0].replace(regexToReplace, '');
      return [aliasKey, root(aliasValue)];
    })
    .reduce((aliases, [key, value]) => {
      aliases[key] = value;
      return aliases;
    }, webpackAliases);
  return webpackAliases;
}

const parseString = require('xml2js').parseString;
// return the version number from `pom.xml` file
function parseVersion() {
  let version = null;
  const pomXml = fs.readFileSync('pom.xml', 'utf8');
  parseString(pomXml, (err, result) => {
    if (result.project.version && result.project.version[0]) {
      version = result.project.version[0];
    } else if (result.project.parent && result.project.parent[0] && result.project.parent[0].version && result.project.parent[0].version[0]) {
      version = result.project.parent[0].version[0];
    }
  });
  if (version === null) {
    throw new Error('pom.xml is malformed. No version is defined');
  }
  return version;
}
