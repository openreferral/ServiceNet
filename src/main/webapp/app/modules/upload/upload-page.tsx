import 'filepond/dist/filepond.min.css';
import './upload-page.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button } from 'reactstrap';
import { FilePond, File, registerPlugin } from 'react-filepond';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';

export interface IUploadPageProp extends StateProps, DispatchProps {}

export interface IUploadState {
  files: String[];
  file: any;
}

export class UploadPage extends React.Component<IUploadPageProp, IUploadState> {
  state: IUploadState = {
    files: [],
    file: null
  };

  onAddFile = (error, file) => {
    if (!error) {
      this.setState({
        file
      });
    }
  };

  onRemoveFile = file => {
    this.setState({
      file: null
    });
  };

  render() {
    registerPlugin(FilePondPluginFileValidateType);
    let supportedFileTypes = ['.csv', '.json', 'application/json'];
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
            onaddfile={(error, file) => this.onAddFile(error, file)}
            onremovefile={file => this.onRemoveFile(file)}
            acceptedFileTypes={supportedFileTypes}
            fileValidateTypeDetectType={(source, type) =>
              new Promise((resolve, reject) => {
                resolve(type);
              })
            }
            onupdatefiles={fileItems => {
              this.setState({
                files: fileItems.map(fileItem => fileItem.file)
              });
            }}
          >
            {this.state.files.map(file => (
              <File key={file} src={file} origin="input" />
            ))}
          </FilePond>
          <Button color="primary" disabled={!this.state.file}>
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
