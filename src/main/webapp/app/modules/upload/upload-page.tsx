import 'filepond/dist/filepond.min.css';
import './upload-page.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Button, Input } from 'reactstrap';
import { FilePond, File, registerPlugin } from 'react-filepond';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import { toast } from 'react-toastify';

import { getSystemAccounts } from './upload-page.reducer';

export interface IUploadPageProp extends StateProps, DispatchProps {}

export interface IUploadState {
  files: any[];
  pond: any;
  isUploadDisabled: boolean;
  delimiter: string;
  provider: string;
  isAdmin: boolean;
}

registerPlugin(FilePondPluginFileValidateType);
const supportedFileTypes = ['.csv', 'text/csv', '.json', 'application/json'];
const maxNumberOfFiles = 100;
const valid = 'valid';

export class UploadPage extends React.Component<IUploadPageProp, IUploadState> {
  state: IUploadState = {
    files: [],
    pond: null,
    isUploadDisabled: true,
    delimiter: '',
    provider: '',
    isAdmin: false
  };

  componentDidMount() {
    if (this.props.isAdmin) {
      this.props.getSystemAccounts();
    }
  }

  getToken = () => {
    const value = '; ' + document.cookie;
    const parts = value.split('; ' + 'XSRF-TOKEN' + '=');
    if (parts.length === 2) {
      return parts
        .pop()
        .split(';')
        .shift();
    }
  };

  onAddFile = (error, file) => {
    if (!error) {
      file.setMetadata(valid, true);
    }
    this.setState({
      isUploadDisabled: this.isUploadDisabled()
    });
  };

  appendFilenameToJSON = (json, filename) => {
    const filenamePart = ', "filename" : "' + filename + '"}';
    return json.replace(/.$/, filenamePart);
  };

  uploadAll = () => {
    toast.info('Processing data. Please wait.');
    this.state.pond.processFiles().then(files => {
      const filesArray = [];
      files.forEach(file => {
        filesArray.push(this.appendFilenameToJSON(file.serverId, file.filenameWithoutExtension));
      });
      fetch('/api/map', {
        method: 'POST',
        body: JSON.stringify(filesArray),
        headers: new Headers({
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': this.getToken(),
          PROVIDER: this.state.provider
        })
      }).then(response => {
        if (response.ok) {
          toast.success('Data processed succesfully!');
        } else {
          toast.error(response.statusText);
        }
      });
    });
  };

  isUploadDisabled = () => {
    let result = this.state.files.length === 0;
    this.state.files.forEach(file => {
      if (!file.getMetadata(valid)) {
        result = true;
      }
    });
    return result;
  };

  onUpdateFiles = items => {
    this.setState(
      {
        files: items
      },
      () =>
        this.setState({
          isUploadDisabled: this.isUploadDisabled()
        })
    );
  };

  fileValidateTypeDetectType = (source, type) =>
    new Promise((resolve, reject) => {
      resolve(type);
    });

  delimiterChange = event => {
    this.setState({ delimiter: event.target.value });
  };

  providerChange = event => {
    this.setState({ provider: event.target.value });
  };

  render() {
    const { isAdmin, systemAccounts } = this.props;

    const selectProvider = isAdmin ? (
      <div>
        <br />
        <p className="lead">
          <Translate contentKey="upload.provider" />
        </p>
        <Input className="col-sm-1" value={this.state.provider} type="select" name="select" onChange={this.providerChange}>
          <option />
          {systemAccounts.map(systemAccount => (
            <option value={systemAccount.name} key={systemAccount.id}>
              {systemAccount.name}
            </option>
          ))}
        </Input>
      </div>
    ) : null;

    return (
      <Row>
        <Col>
          <h2>
            <Translate contentKey="upload.title" />
          </h2>
          <p className="lead">
            <Translate contentKey="upload.subtitle" />
          </p>
          <FilePond
            className="dropArea"
            ref={ref => (this.state.pond = ref)}
            allowMultiple
            allowRevert={false}
            maxFiles={maxNumberOfFiles}
            server={{
              url: '/api/file',
              process: {
                headers: {
                  'X-XSRF-TOKEN': this.getToken(),
                  DELIMITER: this.state.delimiter,
                  PROVIDER: this.state.provider
                }
              }
            }}
            instantUpload={false}
            onaddfile={this.onAddFile}
            acceptedFileTypes={supportedFileTypes}
            fileValidateTypeDetectType={this.fileValidateTypeDetectType}
            onupdatefiles={this.onUpdateFiles}
          >
            {this.state.files.map(fileItem => (
              <File key={fileItem.file} src={fileItem.file} origin="input" />
            ))}
          </FilePond>
          <p className="lead">
            <Translate contentKey="upload.delimiter" />
          </p>
          <Input className="col-sm-1" value={this.state.delimiter} type="select" name="select" onChange={this.delimiterChange}>
            <option />
            <option>,</option>
            <option>;</option>
            <option>^</option>
            <option>|</option>
          </Input>
          {selectProvider}
          <br />
          <Button color="primary" disabled={this.state.isUploadDisabled} onClick={this.uploadAll}>
            <Translate contentKey="upload.submit" />
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = (storeState, { isAdmin }: IUploadState) => ({
  isAdmin,
  systemAccounts: storeState.uploadPage.systemAccounts
});

const mapDispatchToProps = { getSystemAccounts };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UploadPage);
