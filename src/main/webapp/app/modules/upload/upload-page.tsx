import 'filepond/dist/filepond.min.css';
import './upload-page.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button, Label, Input } from 'reactstrap';
import { FilePond, File, registerPlugin } from 'react-filepond';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { faThList } from '@fortawesome/free-solid-svg-icons/faThList';

export interface IUploadPageProp extends StateProps, DispatchProps {}

export interface IUploadState {
  files: any[];
  pond: any;
  isUploadDisabled: boolean;
  delimiter: symbol;
  provider: string;
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
    delimiter: null,
    provider: null
  };

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

  uploadAll = () => {
    this.state.pond.processFiles();
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
          <br />
          <p className="lead">
            <Translate contentKey="upload.provider" />
          </p>
          <Input className="col-sm-1" value={this.state.provider} type="select" name="select" onChange={this.providerChange}>
            <option />
            <option>FirstProvider</option>
            <option>SecondProvider</option>
          </Input>
          <br />
          <Button color="primary" disabled={this.state.isUploadDisabled} onClick={this.uploadAll}>
            <Translate contentKey="upload.submit" />
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UploadPage);
